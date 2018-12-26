package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.*;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.*;

import java.util.*;

public class WagonReviewingService {
    private static final Logger LOG = LogManager.getLogger(WagonReviewingService.class);

    public static List<Ticket> getTickets(int trainId) {
        List<Ticket> tickets = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TicketDao ticketDao = daoFactory.createTicketDao();
             StationDao stationDao = daoFactory.createStationDao()) {
            tickets = ticketDao.findByTrainId(trainId);
            List<Station> stationList = stationDao.findAll();

            tickets.forEach(ticket -> {
                ticket.setArrivalStation(stationList.stream().filter(station ->
                        station.getId() == ticket.getArrivalStationId()).findFirst().orElse(null));
                ticket.setDepartureStation(stationList.stream().filter(station ->
                        station.getId() == ticket.getDepartureStationId()).findFirst().orElse(null));
            });
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return tickets;
    }

    public static List<Wagon> getTrainWagons(int trainId) {
        List<Wagon> wagonList = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (WagonDao wagonDao = daoFactory.createWagonDao()) {
            wagonList = wagonDao.findByTrainId(trainId);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return wagonList;
    }

    public static Train getTrainInfo(int trainId) {
        Train train = new Train();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            train = trainDao.findById(trainId);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return train;
    }
}
