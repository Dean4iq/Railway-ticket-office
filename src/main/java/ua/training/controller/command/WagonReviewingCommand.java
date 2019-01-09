package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Route;
import ua.training.model.entity.Ticket;
import ua.training.model.entity.Train;
import ua.training.model.entity.Wagon;
import ua.training.model.service.WagonReviewingService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class {@code WagonReviewingCommand} provides methods to search seats in
 * wagons for a further ticket purchasing to a user who specifying data in the
 * form on the search page.
 * User can select only unoccupied seats in a wagon.
 * Seats are validating due to the submitted travel date.
 *
 * @author Dean4iq
 * @version 1.0
 */
public class WagonReviewingCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(WagonReviewingCommand.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    /**
     * Listens for requests and provides methods to process them
     *
     * @param request provides user data to process and link to session and
     *                context
     * @return link to the seats search page or to purchase page if transaction is
     * exists
     */
    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("WagonReviewingService execute()");

        HttpSession session = request.getSession();

        if (checkPurchaseSubmit(request)) {
            LOG.debug("A ticket exists, redirecting");
            return "redirect: /purchase";
        }

        String tripDate = (String) session
                .getAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE));
        String parameter = (String) session
                .getAttribute(attrManager.getString(AttributeSources.TICKET_PARAMETERS));

        int trainNumber = Integer.parseInt(parameter
                .substring(attrManager.getString(AttributeSources.WAGON_PURCHASE).length()));
        List<Ticket> ticketList = WagonReviewingService.getTickets(trainNumber);
        List<Wagon> wagonList = WagonReviewingService.getTrainWagons(trainNumber);

        ticketList = filterTicketsByDate(ticketList, tripDate);

        checkSeatsStatus(wagonList, ticketList);
        setLocaleCurrency(wagonList, request);

        request.setAttribute(attrManager.getString(AttributeSources.WAGON_LIST), wagonList);
        request.setAttribute(attrManager.getString(AttributeSources.TRAIN_INFO),
                getTrainInfo(request, trainNumber));

        return "/WEB-INF/user/wagons.jsp";
    }

    /**
     * Checks if there is opened purchasing transaction
     *
     * @param request provides link to the session
     * @return true if the transaction exists, otherwise - false.
     */
    private boolean checkPurchaseSubmit(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String parameter = parameterNames.nextElement();
            if (parameter.contains(attrManager.getString(AttributeSources.PURCHASE_SEAT_PARAM))) {
                request.getSession().setAttribute(attrManager.getString(AttributeSources.SEAT_PURCHASE)
                        , parameter);
                return true;
            }
        }

        return (request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE)) != null);
    }

    /**
     * Filters existed tickets list by a selected travel date
     *
     * @param tickets tickets list t filter
     * @param date    selected travel date
     * @return tickets list with tickets belonged to selected travel date
     */
    private List<Ticket> filterTicketsByDate(List<Ticket> tickets, String date) {
        Calendar calendar = Calendar.getInstance();

        putDateStringIntoCalendar(calendar, date);

        return tickets.stream().filter(ticket -> {
            Calendar ticketDate = Calendar.getInstance();
            ticketDate.setTime(ticket.getTravelDate());

            return (calendar.get(Calendar.YEAR) == ticketDate.get(Calendar.YEAR)
                    && calendar.get(Calendar.MONTH) == ticketDate.get(Calendar.MONTH)
                    && calendar.get(Calendar.DAY_OF_MONTH) == ticketDate.get(Calendar.DAY_OF_MONTH));
        }).collect(Collectors.toList());
    }

    /**
     * Initializes Calendar fields with date fields gained from date string
     *
     * @param calendar a calendar that should be set up
     * @param date     date in format yyyy-MM-dd to initialize calendar
     */
    private void putDateStringIntoCalendar(Calendar calendar, String date) {
        String[] dates = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
    }

    /**
     * Checks if seats are available with existed tickets to this date
     *
     * @param wagons  wagons list with seats inside
     * @param tickets tickets filtered to this date
     */
    private void checkSeatsStatus(List<Wagon> wagons, List<Ticket> tickets) {
        wagons.forEach(wagon ->
                wagon.getSeatList().forEach(seat ->
                        tickets.forEach(ticket -> {
                            if (seat.getId() == ticket.getSeatId()) {
                                seat.setOccupied(true);
                            }
                        })
                )
        );
    }

    /**
     * Receives info about the train that was selected by a user for the travel
     * and set up train route date and time
     *
     * @param request     provides methods to write attributes for the page and
     *                    contains info about selected route and  travel date
     * @param trainNumber number of the selected train
     * @return train with all info necessary
     */
    private Train getTrainInfo(HttpServletRequest request, int trainNumber) {
        HttpSession session = request.getSession();

        String tripDate = (String) session
                .getAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE));
        String stationFrom = (String) session
                .getAttribute(attrManager.getString(AttributeSources.STATION_DEPARTURE));
        String stationTo = (String) session
                .getAttribute(attrManager.getString(AttributeSources.STATION_ARRIVAL));
        String language = (String) session.getAttribute(attrManager.getString(AttributeSources.LANGUAGE));
        Train train = WagonReviewingService.getTrainInfo(trainNumber);

        setTravelDateTime(train, tripDate, language);
        setUserRoute(train, stationFrom, stationTo);

        return train;
    }

    /**
     * Creates and sets to the train user route from and to stations selected
     * by a user
     *
     * @param train with route to set up
     * @param from  selected by a user departure station
     * @param to    selected by a user arrival station
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
     * Sets for a whole route date and time by values from selected travel date
     * in a local variable
     *
     * @param train    train with the route
     * @param date     selected travel date
     * @param language the session language to create locale
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
     * Formats date and time for Timestamp to the local variable
     *
     * @param dateTime   provided date and time to format
     * @param dateFormat format rule to convert date
     * @param timeFormat format rule to convert time
     * @return string formatted date
     */
    private String setFormattedDateTime(Timestamp dateTime, DateFormat dateFormat,
                                        DateFormat timeFormat) {
        Date date = new Date(dateTime.getTime());

        return dateFormat.format(date) + "*" + timeFormat.format(date);
    }

    /**
     * Set calendar year, month, day according to provided String and hours,
     * minutes, seconds according to provided Timestamp
     *
     * @param date      provided date in yyyy-MM-dd format
     * @param trainTime provided time
     * @return Calendar instance with date and time from received values
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
     * Calls the method to convert currency value according to a local settings
     *
     * @param wagons  list of wagons with trains cost inside
     * @param request contains session language settings for creating locale
     */
    private void setLocaleCurrency(List<Wagon> wagons, HttpServletRequest request) {
        final String language = (String) request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.LANGUAGE));

        wagons.forEach(wagon -> {
            Train train = wagon.getTrain();
            train.setLocaleCost(moneyFormatter(language, train.getCost()));
        });
    }

    /**
     * Converts currency info into local currency variable (without a currency
     * sign)
     *
     * @param language language to create locale variable for convert
     * @param value    int cost value that should be converted
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
