package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Route;
import ua.training.model.entity.Ticket;
import ua.training.model.entity.Train;
import ua.training.model.entity.Wagon;
import ua.training.model.service.WagonReviewingService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class WagonReviewingCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(WagonReviewingCommand.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("WagonReviewingService execute()");

        HttpSession session = request.getSession();

        if (checkPurchaseSubmit(request)) {
            LOG.debug("A ticket exists, redirecting");
            return "redirect: /purchase";
        }

        String tripDate = (String) session
                .getAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE));
        String parameter = (String) session
                .getAttribute(attrManager.getString(AttributeSources.TICKET_PARAMETERS));

        int trainNumber = Integer.parseInt(parameter
                .substring(attrManager.getString(AttributeSources.WAGON_PURCHASE).length()));
        List<Ticket> ticketList = WagonReviewingService.getTickets(trainNumber);
        List<Wagon> wagonList = WagonReviewingService.getTrainWagons(trainNumber);

        ticketList = filterTicketsByDate(ticketList, tripDate);

        checkSeatsStatus(wagonList, ticketList);
        setLocaleCurrency(wagonList, request);

        request.setAttribute(attrManager.getString(AttributeSources.WAGON_LIST), wagonList);
        request.setAttribute(attrManager.getString(AttributeSources.TRAIN_INFO),
                getTrainInfo(request, trainNumber));

        return "/WEB-INF/user/wagons.jsp";
    }

    private boolean checkPurchaseSubmit(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String parameter = parameterNames.nextElement();
            if (parameter.contains(attrManager.getString(AttributeSources.PURCHASE_SEAT_PARAM))) {
                request.getSession().setAttribute(attrManager.getString(AttributeSources.SEAT_PURCHASE)
                        , parameter);
                return true;
            }
        }

        return (request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE)) != null);
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
        HttpSession session = request.getSession();

        String tripDate = (String) session
                .getAttribute(attrManager.getString(AttributeSources.DATE_TRIP_PURCHASE));
        String stationFrom = (String) session
                .getAttribute(attrManager.getString(AttributeSources.STATION_DEPARTURE));
        String stationTo = (String) session
                .getAttribute(attrManager.getString(AttributeSources.STATION_ARRIVAL));
        String language = (String) session.getAttribute(attrManager.getString(AttributeSources.LANGUAGE));
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

        return dateFormat.format(date) + "*" + timeFormat.format(date);
    }

    private Calendar setUpCalendar(String date, Timestamp trainTime) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.setTimeInMillis(trainTime.getTime());

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private void setLocaleCurrency(List<Wagon> wagons, HttpServletRequest request) {
        final String language = (String) request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.LANGUAGE));

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
