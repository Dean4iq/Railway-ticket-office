package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Station;
import ua.training.model.entity.Ticket;
import ua.training.model.entity.Train;
import ua.training.model.entity.Wagon;
import ua.training.model.service.PurchaseService;
import ua.training.util.JsonCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PurchaseCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(PurchaseCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");

        if (request.getParameter("payForTicket") != null) {
            acceptPurchasing(request);
            return "redirect: /";
        } else if (request.getParameter("declinePayment") != null) {
            declinePurchasing(request);
            return "redirect: /";
        }

        if (request.getSession().getAttribute("seatToPurchase") != null) {
            String seatParameter = (String) request.getSession().getAttribute("seatToPurchase");
            String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");

            int seatNumber = Integer.parseInt(seatParameter.substring("PurchaseSeat".length()));
            Calendar travelDate = getCalendarForDate(tripDate);

            if (request.getSession().getAttribute("Ticket") == null
                    && PurchaseService.isSeatFree(seatNumber, travelDate)) {
                Ticket ticket = initializeTicket(request, seatNumber, travelDate);

                request.getSession().setAttribute("Ticket", ticket);
                PurchaseService.startPurchaseTransaction(ticket, request);
            } else if (!PurchaseService.isSeatFree(seatNumber, travelDate)
                    && request.getSession().getAttribute("Ticket") == null) {
                return "redirect: /exception";
            }

            request.setAttribute("purchasedTicket", request.getSession().getAttribute("Ticket"));

            try {
                Ticket ticket = (Ticket) request.getSession().getAttribute("Ticket");
                Map<String, Ticket> map = new HashMap<>();
                map.put("purchasedTicket", ticket);
                response.getWriter().write(JsonCreator.getDto(map));
            } catch (IOException e) {
                LOG.error("Error accessing response writer: {}", e);
            }

            return "/WEB-INF/user/purchase.jsp";
        }
        return "redirect: /tickets";
    }

    private void acceptPurchasing(HttpServletRequest request) {
        PurchaseService.confirmPurchasing(request);
        processPageAttributes(request);
    }

    private void declinePurchasing(HttpServletRequest request) {
        PurchaseService.declinePurchasing((Connection) request.getSession().getAttribute("ticketConnection"));
        processPageAttributes(request);
    }

    private void processPageAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute("Ticket");
        request.getSession().removeAttribute("seatToPurchase");
        request.getSession().removeAttribute("tripDateSubmitted");
    }

    private Ticket initializeTicket(HttpServletRequest request, int seatNumber,
                                    Calendar travelDate) {
        Ticket ticket = new Ticket();

        String parameter = (String) request.getSession().getAttribute("searchTicketParameter");
        String departureStationName = (String) request.getSession().getAttribute("departureStation");
        String arrivalStationName = (String) request.getSession().getAttribute("arrivalStation");
        String login = (String) request.getSession().getAttribute("User");

        int trainNumber = Integer.parseInt(parameter.substring("wagonInTrain".length()));
        Station departureStation = PurchaseService.pickStationByName(departureStationName);
        Station arrivalStation = PurchaseService.pickStationByName(arrivalStationName);
        Train train = PurchaseService.getTrain(trainNumber);
        Wagon wagon = PurchaseService.getWagonBySeatId(seatNumber);

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
        ticket.setWagonId(wagon.getId());

        return ticket;
    }

    private Calendar getCalendarForDate(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }


}
