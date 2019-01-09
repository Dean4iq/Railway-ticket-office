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

public class SearchTicketCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SearchTicketCommand.class);
    private static final Map<String, MethodProvider> COMMANDS = new HashMap<>();
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

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

    private boolean checkExistedTickets(HttpServletRequest request) {
        return (request.getSession().getAttribute(attrManager
                .getString(AttributeSources.TICKET_PURCHASE)) != null);
    }

    private void ticketSearchSubmit(HttpServletRequest request) {
        List<Train> trainList = SearchTicketService.findTrainByRoute(request);

        setForRouteLocaleTime(trainList, request);

        request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trainList);
        request.getSession().setAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE),
                request.getParameter(attrManager.getString(AttributeSources.TRIP_START_DATE_PARAM)));
    }

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

    private void setCalendar(HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        String minCalendarDate = getFormattedDate(calendar);
        final int AVAILABLE_DAYS = 1_296_000_000; // 15 days

        calendar.setTimeInMillis(new Date().getTime() + AVAILABLE_DAYS);

        String maxCalendarDate = getFormattedDate(calendar);

        request.setAttribute(attrManager.getString(AttributeSources.MIN_DATE_SEARCH), minCalendarDate);
        request.setAttribute(attrManager.getString(AttributeSources.MAX_DATE_SEARCH), maxCalendarDate);
    }

    private String getFormattedDate(Calendar calendar) {
        return new StringBuilder()
                .append(calendar.get(Calendar.YEAR))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.MONTH) + 1))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.DATE))).toString();
    }

    private String setDateZeroCharacter(int date) {
        if (date < 10) {
            return "0" + date;
        }
        return Integer.toString(date);
    }

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

    private String setFormattedDateTime(Timestamp dateTime, DateFormat dateFormat,
                                        DateFormat timeFormat) {
        Date date = new Date(dateTime.getTime());

        return dateFormat.format(date) + "*" + timeFormat.format(date);
    }
}
