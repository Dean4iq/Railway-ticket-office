package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.Route;
import ua.training.model.entity.Train;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class SearchTicketService implements Service {
    private static final Logger LOG = LogManager.getLogger(SearchTrainService.class);
    private static final Map<String, SearchTicketService> COMMANDS = new HashMap<>();

    @Override
    public String execute(HttpServletRequest request) {
        if (request.getParameter("SwitchDirections") != null) {
            //TODO
        } else if (request.getParameter("ticketSearchSubmit") != null) {
            List<Train> trainList = findTrainByRoute(request);

            request.setAttribute("trainList", trainList);
            request.getSession().setAttribute("tripDateSubmitted", request.getParameter("tripStartDate"));
        }

        if (request.getParameter("sortByIdAsc") != null) {
            List<Train> trainList = findTrainByRoute(request);

            trainList.sort(Comparator.comparingInt(Train::getId));
        }

        if (request.getParameter("sortByIdDesc") != null) {
            List<Train> trainList = findTrainByRoute(request);

            trainList.sort(Comparator.comparingInt(Train::getId).reversed());
        }

        if (checkSubmittedTrain(request)) {
            return "redirect: /wagons";
        }

        Calendar calendar = Calendar.getInstance();
        String minCalendarDate = getFormattedDate(calendar);

        calendar.setTimeInMillis(new Date().getTime() + 1_296_000_000);

        String maxCalendarDate = getFormattedDate(calendar);

        request.setAttribute("minCalendarDate", minCalendarDate);
        request.setAttribute("maxCalendarDate", maxCalendarDate);
        LOG.debug("SearchTrainService execute()");

        return "/WEB-INF/user/searchToPurchase.jsp";
    }

    private List<Train> findTrainByRoute(HttpServletRequest request) {
        String stationFrom = request.getParameter("departureStation");
        String stationTo = request.getParameter("destinationStation");
        List<Train> trains = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            trains = trainDao.findAll();

            trains = trains.stream().filter(train ->
                    checkTrainRoute(train, stationFrom, stationTo))
                    .collect(Collectors.toList());

            setTravelDate(request, trains);

            request.getSession().setAttribute("departureStation", stationFrom);
            request.getSession().setAttribute("arrivalStation", stationTo);
            request.setAttribute("trainList", trains);
            LOG.debug("Search all trains by route");
        } catch (Exception e) {
            LOG.debug("Exception while execute() find Train by Route");
            LOG.error("Exception while execute() find Train by Route: {}",
                    Arrays.toString(e.getStackTrace()));
        }

        return trains;
    }

    private boolean checkTrainRoute(Train train, String from, String to) {
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

    private String getFormattedDate(Calendar calendar) {
        return new StringBuilder()
                .append(calendar.get(Calendar.YEAR))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.MONTH) + 1))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.DATE))).toString();
    }

    private void setTravelDate(HttpServletRequest request, List<Train> trains) {
        Calendar calendar = Calendar.getInstance();
        String travelDate = request.getParameter("tripStartDate");
        String[] datesYMD = travelDate.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(datesYMD[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(datesYMD[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datesYMD[2]));


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
}
