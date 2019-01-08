package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.util.Pagination;
import ua.training.model.entity.Route;
import ua.training.model.entity.Train;
import ua.training.model.service.MethodProvider;
import ua.training.model.service.SearchTrainService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SearchCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SearchCommand.class);
    private static final Map<String, MethodProvider> COMMANDS = new HashMap<>();

    public SearchCommand() {
        COMMANDS.putIfAbsent("trainNumberSubmit", this::findByTrainId);
        COMMANDS.putIfAbsent("trainDestinationSubmit", this::findTrainByRoute);
        COMMANDS.putIfAbsent("allTrainSubmit", this::findAllTrains);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String parameter = params.nextElement();
            if (COMMANDS.containsKey(parameter)) {
                COMMANDS.get(parameter).call(request);
            }
        }

        SearchTrainService.setStationList(request);

        return "/searchTrains.jsp";
    }

    private void findByTrainId(HttpServletRequest request) {
        int trainNumber = Integer.parseInt(request.getParameter("trainNumber"));

        List<Train> trains = SearchTrainService.findTrainById(trainNumber);

        setTravelDate(trains);
        setForRouteLocaleTime(trains, request);

        request.setAttribute("trainList", trains);
        LOG.debug("Search trains by id");
    }

    private void findTrainByRoute(HttpServletRequest request) {
        String stationFrom = request.getParameter("departureStation");
        String stationTo = request.getParameter("destinationStation");

        List<Train> trains = SearchTrainService.findAllTrains();

        trains = trains.stream().filter(train ->
                checkTrainRoute(train, stationFrom, stationTo))
                .collect(Collectors.toList());

        setTravelDate(trains);
        setForRouteLocaleTime(trains, request);

        request.setAttribute("trainList", trains);
        LOG.debug("Search trains by route");
    }

    private void findAllTrains(HttpServletRequest request) {
        List<Train> trains = SearchTrainService.findAllTrains();

        setTravelDate(trains);
        setForRouteLocaleTime(trains, request);

        request.setAttribute("trainList", trains);
        LOG.debug("Search all trains");
    }

    private static boolean checkTrainRoute(Train train, String from, String to) {
        LOG.debug("Check trains by route");

        List<Route> trainRoute = train.getRouteList();

        for (int i = 0; i < trainRoute.size() - 1; i++) {
            if (trainRoute.get(i).getStation().getNameEN().equals(from)
                    || trainRoute.get(i).getStation().getNameUA().equals(from)) {
                for (int j = i + 1; j < trainRoute.size(); j++) {
                    if (trainRoute.get(j).getStation().getNameEN().equals(to)
                            || trainRoute.get(j).getStation().getNameUA().equals(to)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private void setTravelDate(List<Train> trains) {
        Calendar calendar = Calendar.getInstance();

        for (Train train : trains) {
            Timestamp firstStationDepartureTime = train.getDepartureRoute().getDepartureTime();
            Calendar departureCalendar = Calendar.getInstance();

            departureCalendar.setTimeInMillis(firstStationDepartureTime.getTime());

            calendar.set(Calendar.HOUR_OF_DAY, departureCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, departureCalendar.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, departureCalendar.get(Calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, departureCalendar.get(Calendar.MILLISECOND));

            train.getStations().forEach(route -> {
                Calendar arrivalTime = Calendar.getInstance();
                Calendar departureTime = Calendar.getInstance();

                arrivalTime.setTimeInMillis(route.getArrivalTime().getTime());
                departureTime.setTimeInMillis(route.getDepartureTime().getTime());

                long arrivalTimeDiffer = arrivalTime.getTimeInMillis() - firstStationDepartureTime.getTime();
                long departureTimeDiffer = departureTime.getTimeInMillis() - firstStationDepartureTime.getTime();

                route.setArrivalTime(new Timestamp(calendar.getTimeInMillis() + arrivalTimeDiffer));
                route.setDepartureTime(new Timestamp(calendar.getTimeInMillis() + departureTimeDiffer));
            });
        }

        LOG.debug("Set travel date");
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

    private int countPages(List<Train> trains){
        return new Pagination<Train>().getPageNumber(trains);
    }

    private List<Train> paginateList(List<Train> trains, int page) {
        return new Pagination<Train>().getPageList(trains, page);
    }
}
