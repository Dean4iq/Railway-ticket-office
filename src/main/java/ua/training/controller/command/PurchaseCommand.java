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
 * Class {@code PurchaseCommand} exists to provide to the user purchasing
 * information and set the transaction to purchase a ticket.
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
     * @param request
     * @return
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

    private void acceptPurchasing(HttpServletRequest request) {
        PurchaseService.confirmPurchasing(request);
        processPageAttributes(request);
        LOG.debug("Purchasing accepted");
    }

    private void declinePurchasing(HttpServletRequest request) {
        PurchaseService.declinePurchasing((Connection) request.getSession().getAttribute("ticketConnection"));
        processPageAttributes(request);
        LOG.debug("Purchasing declined");
    }

    private void processPageAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.removeAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE));
        session.removeAttribute(attrManager.getString(AttributeSources.SEAT_PURCHASE));
        session.removeAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE));
        session.removeAttribute(attrManager.getString(AttributeSources.TICKET_CONNECTION));
    }

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

    private Calendar getCalendarForDate(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private void setLocaleTime(Ticket ticket, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String lang = (String) session.getAttribute(attrManager.getString(AttributeSources.LANGUAGE));
        String tripDate = (String) session.getAttribute(attrManager
                .getString(AttributeSources.DATE_TRIP_PURCHASE));
        Locale locale = new Locale(lang);
        Train train = ticket.getTrain();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

        ticket.setFormattedTravelDate(dateFormat.format(setCalendar(tripDate).getTime()));

        train.getRouteList().forEach(route -> {
            route.setLocaleArrivalTime(setFormattedDateTime(route.getArrivalTime(),
                    dateFormat, timeFormat));
            route.setLocaleDepartureTime(setFormattedDateTime(route.getDepartureTime(),
                    dateFormat, timeFormat));
        });
    }

    private Calendar setCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private String setFormattedDateTime(Timestamp dateTime, DateFormat dateFormat,
                                        DateFormat timeFormat) {
        java.util.Date date = new java.util.Date(dateTime.getTime());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dateFormat.format(date))
                .append("*")
                .append(timeFormat.format(date));

        return stringBuilder.toString();
    }

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

    private Calendar setUpCalendar(String date, Timestamp trainTime) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.setTimeInMillis(trainTime.getTime());

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private void setLocaleCurrency(Ticket ticket, HttpServletRequest request) {
        final String language = (String) request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.LANGUAGE));
        ticket.setLocaleCost(moneyFormatter(language, ticket.getCost()));
    }

    private String moneyFormatter(String language, int value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale(language));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();

        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(value).trim();
    }
}
