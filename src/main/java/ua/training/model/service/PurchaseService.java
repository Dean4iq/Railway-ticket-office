package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.training.model.dao.*;
import ua.training.model.dao.implementation.JDBCDaoFactory;
import ua.training.model.entity.*;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseService {
    private static final Logger LOG = LogManager.getLogger(PurchaseService.class);

    public static boolean isSeatFree(int seatId, Calendar travelDate) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TicketDao ticketDao = daoFactory.createTicketDao()) {
            List<Ticket> tickets = ticketDao.findAll();
            tickets = getTicketsBySeat(tickets, seatId);
            tickets = getTicketsByDate(tickets, travelDate);

            if (tickets.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return false;
    }

    private static List<Ticket> getTicketsBySeat(List<Ticket> tickets, int seatId) {
        return tickets.stream().filter(ticket ->
                ticket.getSeatId() == seatId).collect(Collectors.toList());
    }

    private static List<Ticket> getTicketsByDate(List<Ticket> tickets, Calendar travelDate) {
        return tickets.stream().filter(ticket -> {
            Calendar ticketTravelDate = Calendar.getInstance();
            ticketTravelDate.setTimeInMillis(ticket.getTravelDate().getTime());
            return (travelDate.get(Calendar.YEAR) == ticketTravelDate.get(Calendar.YEAR)
                    && travelDate.get(Calendar.MONTH) == ticketTravelDate.get(Calendar.MONTH)
                    && travelDate.get(Calendar.DAY_OF_MONTH) == ticketTravelDate.get(Calendar.DAY_OF_MONTH));
        }).collect(Collectors.toList());
    }

    public static Station pickStationByName(String stationName) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        List<Station> stationList = new ArrayList<>();
        try (StationDao stationDao = daoFactory.createStationDao()) {
            stationList = stationDao.findAll();


        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return stationList.stream().filter(station ->
                station.getNameEN().equals(stationName)
                        || station.getNameUA().equals(stationName))
                .findFirst().orElse(null);
    }

    public static Train getTrain(int trainId) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        Train train = new Train();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            train = trainDao.findById(trainId);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return train;
    }

    public static Wagon getWagonBySeatId(int seatId) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        Wagon wagon = new Wagon();

        try (SeatDao seatDao = daoFactory.createSeatDao();
             WagonDao wagonDao = daoFactory.createWagonDao()) {
            Seat seat = seatDao.findById(seatId);
            wagon = wagonDao.findById(seat.getWagonId());
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return wagon;
    }

    public static void startPurchaseTransaction(Ticket ticket, HttpServletRequest request) {
        HttpSession session = request.getSession();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TicketDao ticketDao = daoFactory.createTicketDao()) {
            Connection connection = ticketDao.createWithoutCommit(ticket);
            session.setAttribute("ticketConnection", connection);

            new Thread(() -> {
                try {
                    Thread.sleep(600_000); //Wait 10 minutes for purchasing
                    new TransactionCommit().rollbackAndClose(connection);

                    session.removeAttribute("Ticket");
                    LOG.debug("10 minutes have passed, Transaction rollback and Connection closed");
                } catch (InterruptedException e) {
                    LOG.error("Interrupted thread: {}", Arrays.toString(e.getStackTrace()));
                }
            }).start();

        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void confirmPurchasing(HttpServletRequest request) {
        Connection connection = (Connection) request.getSession().getAttribute("ticketConnection");
        new TransactionCommit().commitAndClose(connection);
    }

    public static void declinePurchasing(Connection connection) {
        new TransactionCommit().rollbackAndClose(connection);
    }
}
