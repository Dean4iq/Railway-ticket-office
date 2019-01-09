package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.*;
import ua.training.model.service.PurchaseService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

/**
 * Class {@code PurchaseCommand} provides methods to provide to the user
 * purchasing information and set the transaction to purchase a ticket.
 * Purchasing info should be available after redirecting to other pages and
 * return to purchase page
 *
 * @author Dean4iq
 * @version 1.0
 */
public class PurchaseCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(PurchaseCommand.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    /**
     * Initializes ticket info for purchasing and listens for user decision and
     * redirects to other pages depending on conditions.
     *
     * @param request provides user date to process and link to session and context
     * @return link to homepage after accepting or declining purchasing by user
     *         or link to purchasing page after initializing ticket info (or on
     *         return, refresh, etc.) or to ticket search page if the time for
     *         purchasing is out.
     */
    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("execute()");

        if (request.getParameter(attrManager
                .getString(AttributeSources.ACCEPT_PURCHASING_PARAM)) != null) {
            acceptPurchasing(request);
            return "redirect: /";
        } else if (request.getParameter(attrManager
                .getString(AttributeSources.DECLINE_PURCHASING_PARAM)) != null) {
            declinePurchasing(request);
            return "redirect: /";
        }

        if (request.getSession().getAttribute(attrManager
                .getString(AttributeSources.SEAT_PURCHASE)) != null) {
            return setTicketData(request);
        }
        return "redirect: /tickets";
    }

    /**
     * Checks if seat data for purchasing and ticket data are already exists
     *
     * @param request provides user data and links to session
     * @return link to purchase page after successful data processing or link
     *         to error page if seat is occupied
     */
    private String setTicketData(HttpServletRequest request) {
        LOG.debug("Setting up ticket");
        HttpSession session = request.getSession();

        String seatParameter = (String) session.getAttribute(attrManager
                .getString(AttributeSources.SEAT_PURCHASE));
        String tripDate = (String) session.getAttribute(attrManager
                .getString(AttributeSources.DATE_TRIP_PURCHASE));

        int seatNumber = Integer.parseInt(seatParameter.substring(attrManager
                .getString(AttributeSources.PURCHASE_SEAT_PARAM).length()));
        Calendar travelDate = getCalendarForDate(tripDate);

        if (session.getAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE)) == null
                && PurchaseService.isSeatFree(seatNumber, travelDate)) {
            Ticket ticket = initializeTicket(request, seatNumber, travelDate);

            session.setAttribute(attrManager.getString(AttributeSources.TIME_PURCHASE),
                    new java.util.Date().getTime());
            session.setAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE), ticket);
            PurchaseService.startPurchaseTransaction(ticket, request);
        } else if (!PurchaseService.isSeatFree(seatNumber, travelDate)
                && session.getAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE)) == null) {
            session.removeAttribute(attrManager.getString(AttributeSources.SEAT_PURCHASE));
            return "redirect: /exception";
        }

        Ticket ticket = (Ticket) session.getAttribute(attrManager
                .getString(AttributeSources.TICKET_PURCHASE));
        setUpTrain(ticket.getTrain(), request);
        setLocaleTime(ticket, request);
        setLocaleCurrency(ticket, request);

        request.setAttribute(attrManager.getString(AttributeSources.TIMELEFT_PURCHASE),
                session.getAttribute(attrManager.getString(AttributeSources.TIME_PURCHASE)));
        request.setAttribute(attrManager.getString(AttributeSources.PURCHASED_TICKET_PURCHASE),
                session.getAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE)));

        return "/WEB-INF/user/purchase.jsp";
    }

    /**
     * Calls connection commit method to commit the ticket in DB
     *
     * @param request provides data for purchasing
     */
    private void acceptPurchasing(HttpServletRequest request) {
        PurchaseService.confirmPurchasing(request);
        removePageAttributes(request);
        LOG.debug("Purchasing accepted");
    }

    /**
     * Calls connection rollback method to rollback the purchase transaction in DB
     *
     * @param request provides data for purchasing
     */
    private void declinePurchasing(HttpServletRequest request) {
        PurchaseService.declinePurchasing((Connection) request.getSession().getAttribute("ticketConnection"));
        removePageAttributes(request);
        LOG.debug("Purchasing declined");
    }

    /**
     * Removes all purchasing info from session after transaction
     *
     * @param request links to session
     */
    private void removePageAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.removeAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE));
        session.removeAttribute(attrManager.getString(AttributeSources.SEAT_PURCHASE));
        session.removeAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE));
        session.removeAttribute(attrManager.getString(AttributeSources.TICKET_CONNECTION));
    }

    /**
     * Creates ticket with all data about travel requested by user
     *
     * @param request provides all data
     * @param seatNumber seat number in trains wagon to occupy
     * @param travelDate date when travel will begin
     * @return ticket with all data about travel
     */
    private Ticket initializeTicket(HttpServletRequest request, int seatNumber,
                                    Calendar travelDate) {
        HttpSession session = request.getSession();
        Ticket ticket = new Ticket();

        String parameter = (String) session.getAttribute(attrManager
                .getString(AttributeSources.TICKET_PARAMETERS));
        String departureStationName = (String) session.getAttribute(attrManager
                .getString(AttributeSources.STATION_DEPARTURE));
        String arrivalStationName = (String) session.getAttribute(attrManager
                .getString(AttributeSources.STATION_ARRIVAL));
        String login = (String) session.getAttribute(attrManager.getString(AttributeSources.ROLE_USER));

        int trainNumber = Integer.parseInt(parameter.substring(attrManager
                .getString(AttributeSources.WAGON_PURCHASE).length()));
        Station departureStation = PurchaseService.pickStationByName(departureStationName);
        Station arrivalStation = PurchaseService.pickStationByName(arrivalStationName);
        Train train = PurchaseService.getTrain(trainNumber);
        Wagon wagon = PurchaseService.getWagonBySeatId(seatNumber);

        ticket.setTrain(train);
        ticket.setDepartureStation(departureStation);
        ticket.setArrivalStation(arrivalStation);

        ticket.setSeatId(seatNumber);
        ticket.setTrainId(train.getId());
        ticket.setCost(train.getCost());
        ticket.setTravelDate(new Date(travelDate.getTimeInMillis()));
        ticket.setDepartureStationId(departureStation.getId());
        ticket.setArrivalStationId(arrivalStation.getId());
        ticket.setUserLogin(login);
        ticket.setWagonId(wagon.getId());

        return ticket;
    }

    /**
     * Provides the train with all necessary information about travel as
     * submitted travel date, user travel route
     *
     * @param train train that should be filled with travel data
     * @param request provides data about travel
     */
    private void setUpTrain(Train train, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String departureStationName = (String) session.getAttribute(attrManager
                .getString(AttributeSources.STATION_DEPARTURE));
        String arrivalStationName = (String) session.getAttribute(attrManager
                .getString(AttributeSources.STATION_ARRIVAL));
        String tripDate = (String) session.getAttribute(attrManager
                .getString(AttributeSources.DATE_TRIP_PURCHASE));
        String lang = (String) session.getAttribute(attrManager.getString(AttributeSources.LANGUAGE));

        setUserRoute(train, departureStationName, arrivalStationName);
        setTravelDateTime(train, tripDate, lang);
    }

    /**
     * Formats date in String to date in Calendar
     * date in String should be formatted as yyyy-MM-dd
     *
     * @param date date that should be converted to Calendar
     * @return Calendar with a year, month and day
     */
    private Calendar getCalendarForDate(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    /**
     * Formats ticket submitted travel date to locale language variable
     *
     * @param ticket where locale date should be stored
     * @param request provides language info and travel date
     * @see DateFormat
     */
    private void setLocaleTime(Ticket ticket, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String lang = (String) session.getAttribute(attrManager.getString(AttributeSources.LANGUAGE));
        String tripDate = (String) session.getAttribute(attrManager
                .getString(AttributeSources.DATE_TRIP_PURCHASE));
        Locale locale = new Locale(lang);
        Train train = ticket.getTrain();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

        ticket.setFormattedTravelDate(dateFormat.format(getCalendarForDate(tripDate).getTime()));

        train.getRouteList().forEach(route -> {
            route.setLocaleArrivalTime(setFormattedDateTime(route.getArrivalTime(),
                    dateFormat, timeFormat));
            route.setLocaleDepartureTime(setFormattedDateTime(route.getDepartureTime(),
                    dateFormat, timeFormat));
        });
    }

    /**
     * Formats date and time in Timestamp to String
     *
     * @param dateTime provided date and time
     * @param dateFormat format rule to convert date
     * @param timeFormat format rule to convert time
     * @return string with formatted date and time, separated by *
     */
    private String setFormattedDateTime(Timestamp dateTime, DateFormat dateFormat,
                                        DateFormat timeFormat) {
        java.util.Date date = new java.util.Date(dateTime.getTime());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dateFormat.format(date))
                .append("*")
                .append(timeFormat.format(date));

        return stringBuilder.toString();
    }

    /**
     * Creating user route limited by selected stations
     *
     * @param train contains whole route information
     * @param from the station form where user decided to depart
     * @param to the station to where user decided to arrive
     */
    private void setUserRoute(Train train, String from, String to) {
        List<Route> routeList = train.getRouteList();

        for (int i = 0; i < routeList.size(); i++) {
            if (routeList.get(i).getStation().getNameUA().equals(from)
                    || routeList.get(i).getStation().getNameEN().equals(from)) {

                for (int j = i; j < routeList.size(); j++) {
                    if (routeList.get(j).getStation().getNameUA().equals(to)
                            || routeList.get(j).getStation().getNameEN().equals(to)) {

                        train.setUserRouteList(routeList.subList(i, j + 1));
                        return;
                    }
                }
            }
        }
    }

    /**
     * Sets train departure and arrival time for the each station on the whole
     * route
     *
     * @param train the train that should provide route list for setup
     * @param date ticket travel date
     * @param language selected language settings in session to convert date
     *                 info in national variable
     */
    private void setTravelDateTime(Train train, String date, String language) {
        Locale locale = new Locale(language);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

        train.getRouteList().forEach(route -> {
            Calendar calendar = setUpCalendar(date, route.getDepartureTime());
            route.setDepartureTime(new Timestamp(calendar.getTimeInMillis()));

            calendar = setUpCalendar(date, route.getArrivalTime());
            route.setArrivalTime(new Timestamp(calendar.getTimeInMillis()));

            String formattedDateTime = setFormattedDateTime(route.getDepartureTime(), dateFormat, timeFormat);
            route.setLocaleDepartureTime(formattedDateTime);

            formattedDateTime = setFormattedDateTime(route.getArrivalTime(), dateFormat, timeFormat);
            route.setLocaleArrivalTime(formattedDateTime);
        });

    }

    /**
     * Creates calendar with a year, moth and day from date string field and
     * hours, minutes and seconds from train timestamp
     *
     * @param date provides ticket date in yyyy-MM-dd format
     * @param trainTime provides train time on some event (departure/arrival)
     * @return Calendar with year, month, day, hour, minute and seconds
     */
    private Calendar setUpCalendar(String date, Timestamp trainTime) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.setTimeInMillis(trainTime.getTime());

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    /**
     * Calls method to convert currency info into local currency variable
     *
     * @param ticket contains cost info
     * @param request provides session language info
     */
    private void setLocaleCurrency(Ticket ticket, HttpServletRequest request) {
        final String language = (String) request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.LANGUAGE));
        ticket.setLocaleCost(moneyFormatter(language, ticket.getCost()));
    }

    /**
     * Converts currency info into local currency variable (without a currency
     * sign)
     *
     * @param language language to create locale variable for convert
     * @param value int cost value that should be converted
     * @return converted string with locale rules
     */
    private String moneyFormatter(String language, int value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale(language));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();

        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(value).trim();
    }
}
