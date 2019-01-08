package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Train;
import ua.training.model.service.MethodProvider;
import ua.training.model.service.SearchTicketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

public class SearchTicketCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SearchTicketCommand.class);
    private static final Map<String, MethodProvider> COMMANDS = new HashMap<>();

    public SearchTicketCommand() {
        COMMANDS.putIfAbsent("ticketSearchSubmit", this::ticketSearchSubmit);
        COMMANDS.putIfAbsent("sortTrainNumAsc", this::sortByTrainNumber);
        COMMANDS.putIfAbsent("sortTrainNumDesc", this::sortByTrainNumber);
        COMMANDS.putIfAbsent("sortTimeAsc", this::sortByTime);
        COMMANDS.putIfAbsent("sortTimeDesc", this::sortByTime);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
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
        return (request.getSession().getAttribute("Ticket") != null);
    }

    private void ticketSearchSubmit(HttpServletRequest request) {
        List<Train> trainList = SearchTicketService.findTrainByRoute(request);

        setForRouteLocaleTime(trainList, request);

        request.setAttribute("trainList", trainList);
        request.getSession().setAttribute("tripDateSubmitted", request.getParameter("tripStartDate"));
    }

    private void sortByTrainNumber(HttpServletRequest request) {
        List<Train> trainList = SearchTicketService.findTrainByRoute(request);

        if (request.getParameter("sortTrainNumAsc") != null) {
            trainList.sort(Comparator.comparingInt(Train::getId));
        } else {
            trainList.sort(Comparator.comparingInt(Train::getId).reversed());
        }

        setForRouteLocaleTime(trainList, request);
        request.setAttribute("trainList", trainList);
    }

    private void sortByTime(HttpServletRequest request) {
        List<Train> trainList = SearchTicketService.findTrainByRoute(request);

        if (request.getParameter("sortTimeAsc") != null) {
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
        request.setAttribute("trainList", trainList);
    }

    private void setCalendar(HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        String minCalendarDate = getFormattedDate(calendar);
        final int AVAILABLE_DAYS = 1_296_000_000; // 15 days

        calendar.setTimeInMillis(new Date().getTime() + AVAILABLE_DAYS);

        String maxCalendarDate = getFormattedDate(calendar);

        request.setAttribute("minCalendarDate", minCalendarDate);
        request.setAttribute("maxCalendarDate", maxCalendarDate);
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
            if (parameter.contains("wagonInTrain")) {
                request.getSession().setAttribute("searchTicketParameter", parameter);
                return true;
            }
        }
        return false;
    }

    private void setForRouteLocaleTime(List<Train> trains, HttpServletRequest request) {
        String lang = (String) request.getSession().getAttribute("language");
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
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dateFormat.format(date))
                .append("*")
                .append(timeFormat.format(date));

        return stringBuilder.toString();
    }
}
