package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.StationDao;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.Route;
import ua.training.model.entity.Station;
import ua.training.model.entity.Train;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class SearchTrainService implements Service {
    private static final Logger LOG = LogManager.getLogger(SearchTrainService.class);
    private static final Map<String, MethodProvider> COMMANDS = new HashMap<>();

    public SearchTrainService() {
        COMMANDS.putIfAbsent("trainNumberSubmit", this::findTrainById);
        COMMANDS.putIfAbsent("trainDestinationSubmit", this::findTrainByRoute);
        COMMANDS.putIfAbsent("allTrainSubmit", this::findAllTrains);
    }

    @Override
    public String execute(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String parameter = params.nextElement();
            if (COMMANDS.containsKey(parameter)) {
                COMMANDS.get(parameter).call(request);
            }
        }

        setStationList(request);
        LOG.debug("SearchTrainService execute()");
        return "/searchTrains.jsp";
    }

    private void findTrainById(HttpServletRequest request) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            int trainNumber = Integer.parseInt(request.getParameter("trainNumber"));
            List<Train> trainList = new ArrayList<>();
            trainList.add(trainDao.findById(trainNumber));
            setTravelDate(trainList);
            request.setAttribute("trainList", trainList);
            LOG.debug("Search all trains");
        } catch (Exception e) {
            LOG.debug("Exception while execute() Find by id");
            LOG.error("Exception while execute() Find by id: {}",
                    Arrays.toString(e.getStackTrace()));
        }
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
    }

    private void findTrainByRoute(HttpServletRequest request) {
        String stationFrom = request.getParameter("departureStation");
        String stationTo = request.getParameter("destinationStation");
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            List<Train> trains = trainDao.findAll();

            trains = trains.stream().filter(train ->
                    checkTrainRoute(train, stationFrom, stationTo))
                    .collect(Collectors.toList());

            setTravelDate(trains);

            request.setAttribute("trainList", trains);
            LOG.debug("Search all trains");
        } catch (Exception e) {
            LOG.debug("Exception while execute() find Train by Route");
            LOG.error("Exception while execute() find Train by Route: {}",
                    Arrays.toString(e.getStackTrace()));
        }
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

    private void findAllTrains(HttpServletRequest request) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            List<Train> trains = trainDao.findAll();
            setTravelDate(trains);
            request.setAttribute("trainList", trains);
            LOG.debug("Search all trains");
        } catch (Exception e) {
            LOG.debug("Exception while execute() allTrainSubmit getAllTrain");
            LOG.error("Exception while execute() allTrainSubmit getAllTrain: {}",
                    Arrays.toString(e.getStackTrace()));
        }
    }

    private void setStationList(HttpServletRequest request) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try(StationDao stationDao = daoFactory.createStationDao()){
            List<Station> stationList = stationDao.findAll();
            request.setAttribute("stationList", stationList);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
