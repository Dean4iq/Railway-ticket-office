package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Ticket;
import ua.training.model.entity.Wagon;
import ua.training.model.service.WagonReviewingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class WagonReviewingCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(WagonReviewingCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("WagonReviewingService execute()");

        if (checkPurchaseSubmit(request)) {
            LOG.debug("A ticket exists, redirecting");
            return "redirect: /purchase";
        }

        String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");
        String parameter = (String) request.getSession().getAttribute("searchTicketParameter");

        int trainNumber = Integer.parseInt(parameter.substring("wagonInTrain".length()));
        List<Ticket> ticketList = WagonReviewingService.getTickets(trainNumber);
        List<Wagon> wagonList = WagonReviewingService.getTrainWagons(trainNumber);

        ticketList = filterTicketsByDate(ticketList, tripDate);

        checkSeatsStatus(wagonList, ticketList);

        request.setAttribute("wagonList", wagonList);
        request.setAttribute("trainInfo", WagonReviewingService.getTrainInfo(trainNumber));

        return "/WEB-INF/user/wagons.jsp";
    }

    private boolean checkPurchaseSubmit(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String parameter = parameterNames.nextElement();
            if (parameter.contains("PurchaseSeat")) {
                request.getSession().setAttribute("seatToPurchase", parameter);
                return true;
            }
        }

        return (request.getSession().getAttribute("Ticket") != null);
    }

    private List<Ticket> filterTicketsByDate(List<Ticket> tickets, String date) {
        Calendar calendar = Calendar.getInstance();

        putDateStringIntoCalendar(calendar, date);

        return tickets.stream().filter(ticket -> {
            Calendar ticketDate = Calendar.getInstance();
            ticketDate.setTime(ticket.getTravelDate());

            return (calendar.get(Calendar.YEAR) == ticketDate.get(Calendar.YEAR)
                    && calendar.get(Calendar.MONTH) == ticketDate.get(Calendar.MONTH)
                    && calendar.get(Calendar.DAY_OF_MONTH) == ticketDate.get(Calendar.DAY_OF_MONTH));
        }).collect(Collectors.toList());
    }

    private void putDateStringIntoCalendar(Calendar calendar, String date) {
        String[] dates = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
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
}
