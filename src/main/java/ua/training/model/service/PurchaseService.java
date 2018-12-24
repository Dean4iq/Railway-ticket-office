package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.StationDao;
import ua.training.model.dao.TicketDao;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.Station;
import ua.training.model.entity.Ticket;
import ua.training.model.entity.Train;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseService implements Service {
    private static final Logger LOG = LogManager.getLogger(PurchaseService.class);

    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("PurchaseClass execute()");

        if (request.getParameter("payForTicket") != null) {
            request.getSession().removeAttribute("Ticket");
            return "redirect: /";
        }
        if (request.getParameter("declinePayment") != null) {
            request.getSession().removeAttribute("Ticket");
            return "redirect: /";
        }

        String seatParameter = (String) request.getSession().getAttribute("seatToPurchase");
        String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");

        int seatNumber = Integer.parseInt(seatParameter.substring("PurchaseSeat".length()));
        Calendar travelDate = getCalendarForDate(tripDate);

        if (request.getSession().getAttribute("Ticket") == null
                && isSeatFree(seatNumber, travelDate)) {
            Ticket ticket = new Ticket();

            String parameter = (String) request.getSession().getAttribute("searchTicketParameter");
            String departureStationName = (String) request.getSession().getAttribute("departureStation");
            String arrivalStationName = (String) request.getSession().getAttribute("arrivalStation");
            String login = (String) request.getSession().getAttribute("User");

            int trainNumber = Integer.parseInt(parameter.substring("wagonInTrain".length()));
            Station departureStation = pickStationByName(departureStationName);
            Station arrivalStation = pickStationByName(arrivalStationName);
            Train train = getTrain(trainNumber);

            ticket.setTrain(train);
            ticket.setDepartureStation(departureStation);
            ticket.setArrivalStation(arrivalStation);

            ticket.setSeatId(seatNumber);
            ticket.setTrainId(train.getId());
            ticket.setCost(train.getCost());
            ticket.setTravelDate(new Date(travelDate.getTimeInMillis()));
            ticket.setDepartureStationId(departureStation.getId());
            ticket.setArrivalStationId(arrivalStation.getId());
            ticket.setUserLogin(login);

            request.getSession().setAttribute("Ticket", ticket);
            startPurchaseTransaction(ticket, request);
        } else if (!isSeatFree(seatNumber, travelDate)) {
            return "redirect: /exception";
        }

        request.setAttribute("purchasedTicket", request.getSession().getAttribute("Ticket"));

        return "/WEB-INF/user/purchase.jsp";
    }

    private Calendar getCalendarForDate(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private boolean isSeatFree(int seatId, Calendar travelDate) {
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

    private List<Ticket> getTicketsBySeat(List<Ticket> tickets, int seatId) {
        return tickets.stream().filter(ticket ->
                ticket.getSeatId() == seatId).collect(Collectors.toList());
    }

    private List<Ticket> getTicketsByDate(List<Ticket> tickets, Calendar travelDate) {
        return tickets.stream().filter(ticket -> {
            Calendar ticketTravelDate = Calendar.getInstance();
            ticketTravelDate.setTimeInMillis(ticket.getTravelDate().getTime());
            return (travelDate.get(Calendar.YEAR) == ticketTravelDate.get(Calendar.YEAR)
                    && travelDate.get(Calendar.MONTH) == ticketTravelDate.get(Calendar.MONTH)
                    && travelDate.get(Calendar.DAY_OF_MONTH) == ticketTravelDate.get(Calendar.DAY_OF_MONTH));
        }).collect(Collectors.toList());
    }

    private Station pickStationByName(String stationName) {
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

    private Train getTrain(int trainId) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        Train train = new Train();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            train = trainDao.findById(trainId);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return train;
    }

    private void startPurchaseTransaction(Ticket ticket, HttpServletRequest request) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        try (TicketDao ticketDao = daoFactory.createTicketDao()) {
            ticketDao.create(ticket);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
