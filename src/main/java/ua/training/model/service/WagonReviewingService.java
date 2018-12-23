package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.*;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class WagonReviewingService implements Service {
    private static final Logger LOG = LogManager.getLogger(WagonReviewingService.class);

    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("WagonReviewingService execute()");
        Calendar calendar = Calendar.getInstance();
        String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");

        putDateStringIntoCalendar(calendar, tripDate);

        String parameter = (String) request.getSession().getAttribute("searchTicketParameter");

        int trainNumber = Integer.parseInt(parameter.substring("wagonInTrain".length()));
        List<Ticket> ticketList = getTickets(trainNumber);
        List<Wagon> wagonList = getTrainWagons(trainNumber);

        ticketList = filterTicketsByDate(ticketList, calendar);

        checkSeatsStatus(wagonList, ticketList);

        request.setAttribute("wagonList", wagonList);
        request.setAttribute("trainInfo", getTrainInfo(trainNumber));

        return "/WEB-INF/user/wagons.jsp";
    }

    private void putDateStringIntoCalendar(Calendar calendar, String date) {
        String[] dates = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
    }

    private List<Ticket> getTickets(int trainId) {
        List<Ticket> tickets = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (TicketDao ticketDao = daoFactory.createTicketDao();
             StationDao stationDao = daoFactory.createStationDao()) {
            tickets = ticketDao.findByTrainId(trainId);
            List<Station> stationList = stationDao.findAll();

            tickets.forEach(ticket -> {
                ticket.setArrivalStation(stationList.stream().filter(station ->
                        station.getId() == ticket.getArrivalStationId()).findFirst().get());
                ticket.setDepartureStation(stationList.stream().filter(station ->
                        station.getId() == ticket.getDepartureStationId()).findFirst().get());
            });
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return tickets;
    }

    private List<Ticket> filterTicketsByDate(List<Ticket> tickets, Calendar calendar) {
        return tickets.stream().filter(ticket -> {
            Calendar ticketDate = Calendar.getInstance();
            ticketDate.setTime(ticket.getTravelDate());

            return (calendar.get(Calendar.YEAR) == ticketDate.get(Calendar.YEAR)
                    && calendar.get(Calendar.MONTH) == ticketDate.get(Calendar.MONTH)
                    && calendar.get(Calendar.DAY_OF_MONTH) == ticketDate.get(Calendar.DAY_OF_MONTH));
        }).collect(Collectors.toList());
    }

    private List<Wagon> getTrainWagons(int trainId) {
        List<Wagon> wagonList = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (WagonDao wagonDao = daoFactory.createWagonDao()) {
            wagonList = wagonDao.findByTrainId(trainId);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return wagonList;
    }

    private void checkSeatsStatus(List<Wagon> wagons, List<Ticket> tickets) {
        wagons.forEach(wagon ->
                wagon.getSeatList().forEach(seat ->
                        tickets.forEach(ticket -> {
                            if (seat.getId() == ticket.getSeatId()) {
                                seat.setOccupied(true);
                            }
                        })
                )
        );
    }

    private Train getTrainInfo(int trainId) {
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
