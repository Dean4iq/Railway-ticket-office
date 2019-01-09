package ua.training.model.entity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class TicketTest {
    private static Date testDate;

    private int initialId = 24;
    private String initialUserLogin = "Godfather";
    private int initialSeatId = 6;
    private int initialTrainId = 12;
    private int initialCost = 450;
    private int initialDepartureStationId = 10;
    private int initialArrivalStationId = 12;
    private int initialWagonId = 42;
    private String formattedTravelDate = "Понеділок, 21 січня 2019 р.";
    private String localeCost = "334,00";

    private Seat testSeat = new Seat();
    private Train testTrain = new Train();
    private Station departureStation = new Station();
    private Station arrivalStation = new Station();

    private Ticket testTicket;

    @BeforeClass
    public static void setDate() {
        testDate = Date.valueOf("2018-02-13");
    }

    @Before
    public void setUp() {
        testTicket = new Ticket();

        testTicket.setId(initialId);
        testTicket.setUserLogin(initialUserLogin);
        testTicket.setSeatId(initialSeatId);
        testTicket.setTrainId(initialTrainId);
        testTicket.setCost(initialCost);
        testTicket.setDepartureStationId(initialDepartureStationId);
        testTicket.setArrivalStationId(initialArrivalStationId);
        testTicket.setTravelDate(testDate);
        testTicket.setWagonId(initialWagonId);
        testTicket.setFormattedTravelDate(formattedTravelDate);
        testTicket.setLocaleCost(localeCost);

        testTicket.setSeat(testSeat);
        testTicket.setTrain(testTrain);
        testTicket.setDepartureStation(departureStation);
        testTicket.setArrivalStation(arrivalStation);
    }

    @Test
    public void getId() {
        assertEquals(initialId, testTicket.getId());
    }

    @Test
    public void setId() {
        int ticketId = 13;

        testTicket.setId(ticketId);

        assertEquals(ticketId, testTicket.getId());
    }

    @Test
    public void getUserLogin() {
        assertEquals(initialUserLogin, testTicket.getUserLogin());
    }

    @Test
    public void setUserLogin() {
        String userLogin = "Rambo";

        testTicket.setUserLogin(userLogin);

        assertEquals(userLogin, testTicket.getUserLogin());
    }

    @Test
    public void getTravelDate() {
        assertEquals(testDate, testTicket.getTravelDate());
    }

    @Test
    public void setTravelDate() {
        Date testDate = new Date(new java.util.Date().getTime());

        testTicket.setTravelDate(testDate);

        assertEquals(testDate, testTicket.getTravelDate());
    }

    @Test
    public void getSeatId() {
        assertEquals(initialSeatId, testTicket.getSeatId());
    }

    @Test
    public void setSeatId() {
        int seatId = 110;

        testTicket.setSeatId(seatId);

        assertEquals(seatId, testTicket.getSeatId());
    }

    @Test
    public void getTrainId() {
        assertEquals(initialTrainId, testTicket.getTrainId());
    }

    @Test
    public void setTrainId() {
        int trainId = 91;

        testTicket.setTrainId(trainId);

        assertEquals(trainId, testTicket.getTrainId());
    }

    @Test
    public void getCost() {
        assertEquals(initialCost, testTicket.getCost());
    }

    @Test
    public void setCost() {
        int cost = 320;

        testTicket.setCost(cost);

        assertEquals(cost, testTicket.getCost());
    }

    @Test
    public void getDepartureStationId() {
        assertEquals(initialDepartureStationId, testTicket.getDepartureStationId());
    }

    @Test
    public void setDepartureStationId() {
        int departureStationId = 11;

        testTicket.setDepartureStationId(departureStationId);

        assertEquals(departureStationId, testTicket.getDepartureStationId());
    }

    @Test
    public void getArrivalStationId() {
        assertEquals(initialArrivalStationId, testTicket.getArrivalStationId());
    }

    @Test
    public void setArrivalStationId() {
        int arrivalStationId = 1;

        testTicket.setArrivalStationId(arrivalStationId);

        assertEquals(arrivalStationId, testTicket.getArrivalStationId());
    }

    @Test
    public void setSeat() {
        Seat seat = new Seat();
        seat.setId(33);

        testTicket.setSeat(seat);

        assertEquals(seat, testTicket.getSeat());
    }

    @Test
    public void getSeat() {
        assertEquals(testSeat, testTicket.getSeat());
    }

    @Test
    public void getTrain() {
        assertEquals(testTrain, testTicket.getTrain());
    }

    @Test
    public void setTrain() {
        Train train = new Train();
        train.setId(111);

        testTicket.setTrain(train);

        assertEquals(train, testTicket.getTrain());
    }

    @Test
    public void getWagonId() {
        assertEquals(initialWagonId, testTicket.getWagonId());
    }

    @Test
    public void setWagonId() {
        int wagonId = 1;

        testTicket.setWagonId(wagonId);

        assertEquals(wagonId, testTicket.getWagonId());
    }

    @Test
    public void getDepartureStation() {
        assertEquals(departureStation, testTicket.getDepartureStation());
    }

    @Test
    public void setDepartureStation() {
        Station departureStation = new Station();

        testTicket.setDepartureStation(departureStation);

        assertEquals(departureStation, testTicket.getDepartureStation());
    }

    @Test
    public void getArrivalStation() {
        assertEquals(arrivalStation, testTicket.getArrivalStation());
    }

    @Test
    public void setArrivalStation() {
        Station arrivalStation = new Station();

        testTicket.setArrivalStation(arrivalStation);

        assertEquals(arrivalStation, testTicket.getArrivalStation());
    }

    @Test
    public void getFormattedTravelDate() {
        assertEquals(formattedTravelDate, testTicket.getFormattedTravelDate());
    }

    @Test
    public void setFormattedTravelDate() {
        String formattedTravelDate = "Субота, 9 лютого 2019 р.";

        testTicket.setFormattedTravelDate(formattedTravelDate);

        assertEquals(formattedTravelDate, testTicket.getFormattedTravelDate());
    }

    @Test
    public void getLocaleCost() {
        assertEquals(localeCost, testTicket.getLocaleCost());
    }

    @Test
    public void setLocaleCost() {
        String localeCost = "123,45";

        testTicket.setLocaleCost(localeCost);

        assertEquals(localeCost, testTicket.getLocaleCost());
    }

    @Test
    public void equalsByHashCode() {
        Ticket ticket = testTicket;

        assertTrue(testTicket.equals(ticket));
    }

    @Test
    public void equalsByFields() {
        Ticket ticket = new Ticket();

        ticket.setId(initialId);
        ticket.setUserLogin(initialUserLogin);
        ticket.setSeatId(initialSeatId);
        ticket.setTrainId(initialTrainId);
        ticket.setCost(initialCost);
        ticket.setDepartureStationId(initialDepartureStationId);
        ticket.setArrivalStationId(initialArrivalStationId);
        ticket.setTravelDate(testDate);
        ticket.setWagonId(initialWagonId);

        ticket.setSeat(testSeat);
        ticket.setTrain(testTrain);
        ticket.setDepartureStation(departureStation);
        ticket.setArrivalStation(arrivalStation);

        assertTrue(testTicket.equals(ticket));
    }

    @Test
    public void notEquals() {
        Ticket ticket = new Ticket();

        ticket.setId(initialId + 1);
        ticket.setUserLogin(initialUserLogin);
        ticket.setSeatId(initialSeatId);
        ticket.setTrainId(initialTrainId);
        ticket.setCost(initialCost);
        ticket.setDepartureStationId(initialDepartureStationId);
        ticket.setArrivalStationId(initialArrivalStationId);
        ticket.setTravelDate(testDate);
        ticket.setWagonId(initialWagonId);

        ticket.setSeat(testSeat);
        ticket.setTrain(testTrain);
        ticket.setDepartureStation(departureStation);
        ticket.setArrivalStation(arrivalStation);

        assertFalse(testTicket.equals(ticket));
    }
}