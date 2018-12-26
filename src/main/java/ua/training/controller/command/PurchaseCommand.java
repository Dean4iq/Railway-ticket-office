package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Station;
import ua.training.model.entity.Ticket;
import ua.training.model.entity.Train;
import ua.training.model.service.PurchaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Calendar;

public class PurchaseCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(PurchaseCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");

        if (request.getParameter("payForTicket") != null) {
            PurchaseService.confirmPurchasing(request);
            request.getSession().removeAttribute("Ticket");
            return "redirect: /";
        }
        if (request.getParameter("declinePayment") != null) {
            PurchaseService.declinePurchasing(request);
            request.getSession().removeAttribute("Ticket");
            return "redirect: /";
        }

        String seatParameter = (String) request.getSession().getAttribute("seatToPurchase");
        String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");

        int seatNumber = Integer.parseInt(seatParameter.substring("PurchaseSeat".length()));
        Calendar travelDate = getCalendarForDate(tripDate);

        if (request.getSession().getAttribute("Ticket") == null
                && PurchaseService.isSeatFree(seatNumber, travelDate)) {
            Ticket ticket = new Ticket();

            String parameter = (String) request.getSession().getAttribute("searchTicketParameter");
            String departureStationName = (String) request.getSession().getAttribute("departureStation");
            String arrivalStationName = (String) request.getSession().getAttribute("arrivalStation");
            String login = (String) request.getSession().getAttribute("User");

            int trainNumber = Integer.parseInt(parameter.substring("wagonInTrain".length()));
            Station departureStation = PurchaseService.pickStationByName(departureStationName);
            Station arrivalStation = PurchaseService.pickStationByName(arrivalStationName);
            Train train = PurchaseService.getTrain(trainNumber);

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
            PurchaseService.startPurchaseTransaction(ticket, request);
        } else if (!PurchaseService.isSeatFree(seatNumber, travelDate)) {
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


}
