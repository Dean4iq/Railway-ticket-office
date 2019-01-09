package ua.training.model.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SeatTest {
    private Seat testSeat;

    private int id = 13;
    private int wagonId = 45;
    private boolean occupied = true;

    private Wagon wagon = new Wagon();

    @Before
    public void setUp() throws Exception {
        testSeat = new Seat();
        wagon.setId(wagonId);

        testSeat.setId(id);
        testSeat.setWagonId(wagonId);
        testSeat.setOccupied(occupied);

        testSeat.setWagon(wagon);
    }

    @Test
    public void getId() {
        assertEquals(id, testSeat.getId());
    }

    @Test
    public void setId() {
        int id = 111;

        testSeat.setId(id);

        assertEquals(id, testSeat.getId());
    }

    @Test
    public void getWagonId() {
        assertEquals(wagonId, testSeat.getWagonId());
    }

    @Test
    public void setWagonId() {
        int wagonId = 311;

        testSeat.setWagonId(wagonId);

        assertEquals(wagonId, testSeat.getWagonId());
    }

    @Test
    public void isOccupied() {
        assertEquals(occupied, testSeat.isOccupied());
    }

    @Test
    public void setOccupied() {
        boolean occupied = false;

        testSeat.setOccupied(occupied);

        assertEquals(occupied, testSeat.isOccupied());
    }

    @Test
    public void getWagon() {
        assertEquals(wagon, testSeat.getWagon());
    }

    @Test
    public void setWagon() {
        Wagon wagon = new Wagon();
        wagon.setId(3);

        testSeat.setWagon(wagon);

        assertEquals(wagon, testSeat.getWagon());
    }

    @Test
    public void notEquals() {
        Seat seat = new Seat();

        assertNotEquals(seat, testSeat);
    }

    @Test
    public void equals() {
        Seat seat = new Seat();

        seat.setId(id);
        seat.setWagonId(wagonId);
        seat.setOccupied(occupied);
        seat.setWagon(wagon);

        assertEquals(seat, testSeat);
    }
}