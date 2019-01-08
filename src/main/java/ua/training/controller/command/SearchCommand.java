package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.util.Pagination;
import ua.training.model.entity.Route;
import ua.training.model.entity.Train;
import ua.training.model.service.MethodProvider;
import ua.training.model.service.SearchTrainService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SearchCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SearchCommand.class);
    private static final Map<String, MethodProvider> COMMANDS = new HashMap<>();
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    public SearchCommand() {
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.SEARCH_BY_TRAIN_NUM),
                this::findByTrainId);
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.SEARCH_BY_ROUTE),
                this::findTrainByRoute);
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.SEARCH_ALL_TRAIN),
                this::findAllTrains);
        COMMANDS.putIfAbsent(attrManager.getString(AttributeSources.SEARCH_PAGE), this::pagingElements);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String parameter = params.nextElement();
            if (COMMANDS.containsKey(parameter)
                    || parameter.contains(attrManager.getString(AttributeSources.SEARCH_PAGE))) {
                parameter = parameter.contains(attrManager.getString(AttributeSources.SEARCH_PAGE))
                        ? attrManager.getString(AttributeSources.SEARCH_PAGE)
                        : parameter;

                COMMANDS.get(parameter).call(request);
                break;
            }
        }

        SearchTrainService.setStationList(request);

        return "/searchTrains.jsp";
    }

    private void findByTrainId(HttpServletRequest request) {
        int trainNumber = Integer.parseInt(request.getParameter(attrManager
                .getString(AttributeSources.TRAIN_NUMBER)));

        List<Train> trains = SearchTrainService.findTrainById(trainNumber);

        setTravelDate(trains);
        setForRouteLocaleTime(trains, request);

        request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trains);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_CURRENT_PAGE), 1);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_PAGE_NUM), 1);

        LOG.debug("Search trains by id");
    }

    private void findTrainByRoute(HttpServletRequest request) {
        Pagination<Train> pagination = new Pagination<>();
        String stationFrom = request.getParameter(attrManager
                .getString(AttributeSources.DEPARTURE_STATION_PARAM));
        String stationTo = request.getParameter(attrManager
                .getString(AttributeSources.DESTINATION_STATION_PARAM));

        List<Train> trains = SearchTrainService.findAllTrains();

        trains = trains.stream().filter(train ->
                checkTrainRoute(train, stationFrom, stationTo))
                .collect(Collectors.toList());

        setTravelDate(trains);
        setForRouteLocaleTime(trains, request);

        int pageNumber = pagination.getPageNumber(trains);
        trains = pagination.getPageList(trains, 1);

        request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trains);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_CURRENT_PAGE), 1);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_PAGE_NUM), pageNumber);
        LOG.debug("Search trains by route");
    }

    private void findAllTrains(HttpServletRequest request) {
        Pagination<Train> pagination = new Pagination<>();
        List<Train> trains = SearchTrainService.findAllTrains();

        setTravelDate(trains);
        setForRouteLocaleTime(trains, request);

        int pageNumber = pagination.getPageNumber(trains);
        trains = pagination.getPageList(trains, 1);

        request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trains);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_CURRENT_PAGE), 1);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_PAGE_NUM), pageNumber);
        LOG.debug("Search all trains");
    }

    private void pagingElements(HttpServletRequest request) {
        Pagination<Train> pagination = new Pagination<>();
        List<Train> trains = SearchTrainService.findAllTrains();
        int page = 1;

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String parameter = params.nextElement();
            if (parameter.contains(attrManager.getString(AttributeSources.SEARCH_PAGE))) {
                page = Integer.parseInt(parameter
                        .substring(attrManager.getString(AttributeSources.SEARCH_PAGE).length()));
                break;
            }
        }

        setTravelDate(trains);
        setForRouteLocaleTime(trains, request);

        int pageNumber = pagination.getPageNumber(trains);
        trains = pagination.getPageList(trains, page);

        request.setAttribute(attrManager.getString(AttributeSources.SEARCH_TRAIN_LIST), trains);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_CURRENT_PAGE), page);
        request.setAttribute(attrManager.getString(AttributeSources.PAGINATE_PAGE_NUM), pageNumber);

        LOG.debug("Paging list");
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
