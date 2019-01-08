package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.*;
import ua.training.model.service.PurchaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

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
            return setTicketData(request);
        }
        return "redirect: /tickets";
    }

    private String setTicketData(HttpServletRequest request) {
        LOG.debug("Setting up ticket");

        String seatParameter = (String) request.getSession().getAttribute("seatToPurchase");
        String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");

        int seatNumber = Integer.parseInt(seatParameter.substring("PurchaseSeat".length()));
        Calendar travelDate = getCalendarForDate(tripDate);

        if (request.getSession().getAttribute("Ticket") == null
                && PurchaseService.isSeatFree(seatNumber, travelDate)) {
            Ticket ticket = initializeTicket(request, seatNumber, travelDate);

            request.getSession().setAttribute("timeForPurchase", new java.util.Date().getTime());
            request.getSession().setAttribute("Ticket", ticket);
            PurchaseService.startPurchaseTransaction(ticket, request);
        } else if (!PurchaseService.isSeatFree(seatNumber, travelDate)
                && request.getSession().getAttribute("Ticket") == null) {
            return "redirect: /exception";
        }

        Ticket ticket = (Ticket) request.getSession().getAttribute("Ticket");
        setUpTrain(ticket.getTrain(), request);
        setLocaleTime(ticket, request);
        setLocaleCurrency(ticket, request);

        request.setAttribute("purchaseTime", request.getSession().getAttribute("timeForPurchase"));
        request.setAttribute("purchasedTicket", request.getSession().getAttribute("Ticket"));

        return "/WEB-INF/user/purchase.jsp";
    }

    private void acceptPurchasing(HttpServletRequest request) {
        PurchaseService.confirmPurchasing(request);
        processPageAttributes(request);
        LOG.debug("Purchasing accepted");
    }

    private void declinePurchasing(HttpServletRequest request) {
        PurchaseService.declinePurchasing((Connection) request.getSession().getAttribute("ticketConnection"));
        processPageAttributes(request);
        LOG.debug("Purchasing declined");
    }

    private void processPageAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute("Ticket");
        request.getSession().removeAttribute("seatToPurchase");
        request.getSession().removeAttribute("tripDateSubmitted");
        request.getSession().removeAttribute("ticketConnection");
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

    private void setUpTrain(Train train, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String departureStationName = (String) session.getAttribute("departureStation");
        String arrivalStationName = (String) session.getAttribute("arrivalStation");
        String tripDate = (String) session.getAttribute("tripDateSubmitted");
        String lang = (String) session.getAttribute("language");

        setUserRoute(train, departureStationName, arrivalStationName);
        setTravelDateTime(train, tripDate, lang);
    }

    private Calendar getCalendarForDate(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private void setLocaleTime(Ticket ticket, HttpServletRequest request) {
        String lang = (String) request.getSession().getAttribute("language");
        String tripDate = (String) request.getSession().getAttribute("tripDateSubmitted");
        Locale locale = new Locale(lang);
        Train train = ticket.getTrain();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

        ticket.setFormattedTravelDate(dateFormat.format(setCalendar(tripDate).getTime()));

        train.getRouteList().forEach(route -> {
            route.setLocaleArrivalTime(setFormattedDateTime(route.getArrivalTime(),
                    dateFormat, timeFormat));
            route.setLocaleDepartureTime(setFormattedDateTime(route.getDepartureTime(),
                    dateFormat, timeFormat));
        });
    }

    private Calendar setCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private String setFormattedDateTime(Timestamp dateTime, DateFormat dateFormat,
                                        DateFormat timeFormat) {
        java.util.Date date = new java.util.Date(dateTime.getTime());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dateFormat.format(date))
                .append("*")
                .append(timeFormat.format(date));

        return stringBuilder.toString();
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

    private Calendar setUpCalendar(String date, Timestamp trainTime) {
        Calendar calendar = Calendar.getInstance();
        String[] splittedDate = date.split("-");

        calendar.setTimeInMillis(trainTime.getTime());

        calendar.set(Calendar.YEAR, Integer.parseInt(splittedDate[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splittedDate[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splittedDate[2]));

        return calendar;
    }

    private void setLocaleCurrency(Ticket ticket, HttpServletRequest request) {
        final String language = (String) request.getSession().getAttribute("language");
        ticket.setLocaleCost(moneyFormatter(language, ticket.getCost()));
    }

    private String moneyFormatter(String language, int value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale(language));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();

        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(value).trim();
    }
}
