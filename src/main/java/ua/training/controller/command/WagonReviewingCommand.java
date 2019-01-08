package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Route;
import ua.training.model.entity.Ticket;
import ua.training.model.entity.Train;
import ua.training.model.entity.Wagon;
import ua.training.model.service.WagonReviewingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
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
        setLocaleCurrency(wagonList, request);

        request.setAttribute("wagonList", wagonList);
        request.setAttribute("trainInfo", getTrainInfo(request, trainNumber));

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

    private Train getTrainInfo(HttpServletRequest request, int trainNumber) {
        String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");
        String stationFrom = (String) request.getSession().getAttribute("departureStation");
        String stationTo = (String) request.getSession().getAttribute("arrivalStation");
        String language = (String) request.getSession().getAttribute("language");
        Train train = WagonReviewingService.getTrainInfo(trainNumber);

        setTravelDateTime(train, tripDate, language);
        setUserRoute(train, stationFrom, stationTo);

        return train;
    }

    private void setUserRoute(Train train, String from, String to) {
        List<Route> routeList = train.getRouteList();

        for (int i = 0; i < routeList.size(); i++) {
            if (routeList.get(i).getStation().getNameUA().equals(from)
                    || routeList.get(i).getStation().getNameEN().equals(from)) {

                for (int j = i; j < routeList.size(); j++) {
                    if (routeList.get(j).getStation().getNameUA().equals(to)
                            || routeList.get(j).getStation().getNameEN().equals(to)) {

                        train.setUserRouteList(routeList.subList(i, j + 1));
                        return;
                    }
                }
            }
        }
    }

    private void setTravelDateTime(Train train, String date, String language) {
        Locale locale = new Locale(language);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

        train.getRouteList().forEach(route -> {
            Calendar calendar = setUpCalendar(date, route.getDepartureTime());
            route.setDepartureTime(new Timestamp(calendar.getTimeInMillis()));

            calendar = setUpCalendar(date, route.getArrivalTime());
            route.setArrivalTime(new Timestamp(calendar.getTimeInMillis()));

            String formattedDateTime = setFormattedDateTime(route.getDepartureTime(), dateFormat, timeFormat);
            route.setLocaleDepartureTime(formattedDateTime);

            formattedDateTime = setFormattedDateTime(route.getArrivalTime(), dateFormat, timeFormat);
            route.setLocaleArrivalTime(formattedDateTime);
        });

    }

    private String setFormattedDateTime(Timestamp dateTime, DateFormat dateFormat,
                                        DateFormat timeFormat) {
        Date date = new Date(dateTime.getTime());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dateFormat.format(date))
                .append("*")
                .append(timeFormat.format(date));

        return stringBuilder.toString();
    }

    private Calendar setUpCalendar(String date, Timestamp trainTime) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.setTimeInMillis(trainTime.getTime());

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private void setLocaleCurrency(List<Wagon> wagons, HttpServletRequest request) {
        final String language = (String) request.getSession().getAttribute("language");
        wagons.forEach(wagon -> {
            Train train = wagon.getTrain();
            train.setLocaleCost(moneyFormatter(language, train.getCost()));
        });
    }

    private String moneyFormatter(String language, int value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale(language));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();

        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(value).trim();
    }
}
