package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Train;
import ua.training.model.service.MethodProvider;
import ua.training.model.service.SearchTicketService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

/**
 * Class {@code SearchTicketCommand} provides methods to search trains in the
 * system for a further ticket purchasing to a user who specifying data in the
 * form on the search page.
 * The search could be submitted only by train route.
 * The class also provides sorting for train list by train number or departure
 * time.
 *
 * @author Dean4iq
 * @version 1.0
 */
public class SearchTicketCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SearchTicketCommand.class);
    private static final Map<String, MethodProvider> COMMANDS = new HashMap<>();
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    /**
     * On exemplar creating there should be a map of methods for corresponding
     * requests
     */
    public SearchTicketCommand() {
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.TICKET_SEARCH_SUBMIT_PARAM),
                this::ticketSearchSubmit);
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.TICKET_SORT_BY_TRAIN_NUM_PARAM_ASC),
                this::sortByTrainNumber);
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.TICKET_SORT_BY_TRAIN_NUM_PARAM_DESC),
                this::sortByTrainNumber);
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.TICKET_SORT_BY_TRAIN_DEPART_PARAM_ASC),
                this::sortByTime);
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.TICKET_SORT_BY_TRAIN_DEPART_PARAM_DESC),
                this::sortByTime);
    }

    /**
     * Listens for requests and provides methods to process them
     *
     * @param request provides user data to process and link to session and
     *                context
     * @return link to the search page or to purchase page if transaction is
     * exists or to the wagon list page if a train is selected
     */
    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("execute()");

        if (checkExistedTickets(request)) {
            LOG.debug("A ticket exists, redirecting");
            return "redirect: /purchase";
        }

        if (checkSubmittedTrain(request)) {
            return "redirect: /wagons";
        }

        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String parameter = params.nextElement();
            if (COMMANDS.containsKey(parameter)) {
                COMMANDS.get(parameter).call(request);
            }
        }

        setCalendar(request);
        SearchTicketService.setStationList(request);

        return "/WEB-INF/user/searchToPurchase.jsp";
    }

    /**
     * Checks if there is opened purchasing transaction
     *
     * @param request provides link to session
     * @return true if purchasing transaction exists, otherwise false
     */
    private boolean checkExistedTickets(HttpServletRequest request) {
        return (request.getSession().getAttribute(attrManager
                .getString(AttributeSources.TICKET_PURCHASE)) != null);
    }

    /**
     * Checks if user has select some train
     *
     * @param request provides parameters from search page
     * @return true if train is selected, otherwise false
     */
    private boolean checkSubmittedTrain(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            if (parameter.contains(attrManager.getString(AttributeSources.WAGON_PURCHASE))) {
                request.getSession().setAttribute(attrManager
                        .getString(AttributeSources.TICKET_PARAMETERS), parameter);
                return true;
            }
        }
        return false;
    }

    /**
     * Searches trains for route between stations, selected by a user.
     * Also for a route sets locale date time value.
     * If trains list by route is absent then the warning occur on the page.
     *
     * @param request contains methods to access attributes and link to the
     *                session and selected language
     */
    private void ticketSearchSubmit(HttpServletRequest request) {
        List<Train> trainList = SearchTicketService.findTrainByRoute(request);

        if (!trainList.isEmpty()) {
            setForRouteLocaleTime(trainList, request);

            request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trainList);
            request.getSession().setAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE),
                    request.getParameter(attrManager.getString(AttributeSources.TRIP_START_DATE_PARAM)));
        } else {
            request.setAttribute(attrManager.getString(AttributeSources.NO_TRAIN_IN_LIST), true);
        }
    }

    /**
     * Sort train by a number (id) ascending or descending, depends on users
     * choice
     * Also for a trains route sets locale date time value.
     *
     * @param request contains methods to access attributes and link to the
     *                session and selected language
     */
    private void sortByTrainNumber(HttpServletRequest request) {
        List<Train> trainList = SearchTicketService.findTrainByRoute(request);

        if (request.getParameter(attrManager
                .getString(AttributeSources.TICKET_SORT_BY_TRAIN_NUM_PARAM_ASC)) != null) {
            trainList.sort(Comparator.comparingInt(Train::getId));
        } else {
            trainList.sort(Comparator.comparingInt(Train::getId).reversed());
        }

        setForRouteLocaleTime(trainList, request);
        request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trainList);
    }

    /**
     * Sort train by a departure time (id) ascending or descending, depends on
     * users choice
     * Also for a trains route sets locale date time value.
     *
     * @param request contains methods to access attributes and link to the
     *                session and selected language
     */
    private void sortByTime(HttpServletRequest request) {
        List<Train> trainList = SearchTicketService.findTrainByRoute(request);

        if (request.getParameter(attrManager
                .getString(AttributeSources.TICKET_SORT_BY_TRAIN_DEPART_PARAM_ASC)) != null) {
            trainList.sort((o1, o2) -> {
                Date date1 = o1.getDepartureRoute().getDepartureTime();
                Date date2 = o2.getDepartureRoute().getDepartureTime();
                return date1.compareTo(date2);
            });
        } else {
            trainList.sort((o1, o2) -> {
                Date date1 = o1.getDepartureRoute().getDepartureTime();
                Date date2 = o2.getDepartureRoute().getDepartureTime();
                return date2.compareTo(date1);
            });
        }

        setForRouteLocaleTime(trainList, request);
        request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trainList);
    }

    /**
     * Sets available min and max calendar days for date picker on the page
     *
     * @param request provides methods to write attributes for the page
     */
    private void setCalendar(HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        String minCalendarDate = getFormattedDate(calendar);
        final int AVAILABLE_DAYS = 1_296_000_000; // 15 days

        calendar.setTimeInMillis(new Date().getTime() + AVAILABLE_DAYS);

        String maxCalendarDate = getFormattedDate(calendar);

        request.setAttribute(attrManager.getString(AttributeSources.MIN_DATE_SEARCH), minCalendarDate);
        request.setAttribute(attrManager.getString(AttributeSources.MAX_DATE_SEARCH), maxCalendarDate);
    }

    /**
     * Formats Calendar value to String
     *
     * @param calendar calendar instance to format
     * @return string date value as yyyy-MM-dd
     */
    private String getFormattedDate(Calendar calendar) {
        return new StringBuilder()
                .append(calendar.get(Calendar.YEAR))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.MONTH) + 1))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.DATE))).toString();
    }

    /**
     * Formats int value to String
     * If int value less than 10 in String writes 0 before int value
     *
     * @param date int value of a date
     * @return int value formatted to string
     */
    private String setDateZeroCharacter(int date) {
        if (date < 10) {
            return "0" + date;
        }
        return Integer.toString(date);
    }

    /**
     * Set locale variation on date time for trains arrival and departure
     * routes
     *
     * @param trains  trains list with routes to set up
     * @param request contains session language settings
     */
    private void setForRouteLocaleTime(List<Train> trains, HttpServletRequest request) {
        String lang = (String) request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.LANGUAGE));
        Locale locale = new Locale(lang);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

        trains.forEach(train ->
                train.getRouteList().forEach(route -> {
                    route.setLocaleArrivalTime(setFormattedDateTime(route.getArrivalTime(),
                            dateFormat, timeFormat));
                    route.setLocaleDepartureTime(setFormattedDateTime(route.getDepartureTime(),
                            dateFormat, timeFormat));
                })
        );
    }

    /**
     * Formats date and time in Timestamp to String
     *
     * @param dateTime   provided date and time
     * @param dateFormat format rule to convert date
     * @param timeFormat format rule to convert time
     * @return string with formatted date and time, separated by *
     */
    private String setFormattedDateTime(Timestamp dateTime, DateFormat dateFormat,
                                        DateFormat timeFormat) {
        Date date = new Date(dateTime.getTime());

        return dateFormat.format(date) + "*" + timeFormat.format(date);
    }
}
