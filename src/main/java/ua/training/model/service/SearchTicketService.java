package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.StationDao;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.implementation.JDBCDaoFactory;
import ua.training.model.entity.Route;
import ua.training.model.entity.Station;
import ua.training.model.entity.Train;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class SearchTicketService {
    private static final Logger LOG = LogManager.getLogger(SearchTrainService.class);
    private static AttributeResourceManager attrManag = AttributeResourceManager.INSTANCE;

    public static List<Train> findTrainByRoute(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String stationFrom = request.getParameter(attrManag
                .getString(AttributeSources.DEPARTURE_STATION_PARAM));
        String stationTo = request.getParameter(attrManag
                .getString(AttributeSources.DESTINATION_STATION_PARAM));

        if (stationFrom == null || stationTo == null) {
            stationFrom = (String) session.getAttribute(attrManag
                    .getString(AttributeSources.STATION_DEPARTURE));
            stationTo = (String) session.getAttribute(attrManag
                    .getString(AttributeSources.STATION_ARRIVAL));
        }

        List<Train> trains = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            trains = trainDao.findAll();

            String finalStationFrom = stationFrom;
            String finalStationTo = stationTo;

            trains = trains.stream().filter(train ->
                    checkTrainRoute(train, finalStationFrom, finalStationTo))
                    .collect(Collectors.toList());

            setTravelDate(request, trains);

            session.setAttribute(attrManag.getString(AttributeSources.STATION_DEPARTURE), stationFrom);
            session.setAttribute(attrManag.getString(AttributeSources.STATION_ARRIVAL), stationTo);
            request.setAttribute(attrManag.getString(AttributeSources.SEARCH_TRAIN_LIST), trains);
            LOG.debug("Search all trains by route");
        } catch (Exception e) {
            LOG.debug("Exception while execute() find Train by Route");
            LOG.error("Exception while execute() find Train by Route: {}",
                    Arrays.toString(e.getStackTrace()));
        }

        return trains;
    }

    private static boolean checkTrainRoute(Train train, String from, String to) {
        List<Route> trainRoute = train.getRouteList();

        for (int i = 0; i < trainRoute.size() - 1; i++) {
            if (trainRoute.get(i).getStation().getNameEN().equals(from)
                    || trainRoute.get(i).getStation().getNameUA().equals(from)) {
                for (int j = i + 1; j < trainRoute.size(); j++) {
                    if (trainRoute.get(j).getStation().getNameEN().equals(to)
                            || trainRoute.get(j).getStation().getNameUA().equals(to)) {
                        train.setUserRouteList(trainRoute.subList(i, j + 1));
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }


    private static void setTravelDate(HttpServletRequest request, List<Train> trains) {
        Calendar calendar = Calendar.getInstance();
        HttpSession session = request.getSession();

        String travelDate = request
                .getParameter(attrManag.getString(AttributeSources.TRIP_START_DATE_PARAM));

        if (travelDate == null) {
            travelDate = (String) session.getAttribute(attrManag.getString(AttributeSources.TRAVEL_DATE));
        }

        session.setAttribute(attrManag.getString(AttributeSources.TRAVEL_DATE), travelDate);

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

    public static void setStationList(HttpServletRequest request) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (StationDao stationDao = daoFactory.createStationDao()) {
            List<Station> stationList = stationDao.findAll();
            request.setAttribute(attrManag.getString(AttributeSources.STATION_LIST), stationList);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
