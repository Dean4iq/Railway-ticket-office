package ua.training.model.entity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WagonTest {
    private Wagon testWagon;

    private int id = 45;
    private int trainId = 11;
    private String typeUA = "Плацкарт";
    private String typeEN = "3rd class";

    private Train train = new Train();
    private List<Seat> seatList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        testWagon = new Wagon();

        testWagon.setId(id);
        testWagon.setTrainId(trainId);
        testWagon.setTypeEN(typeEN);
        testWagon.setTypeUA(typeUA);

        testWagon.setTrain(train);
        testWagon.setSeatList(seatList);
    }

    @Test
    public void getId() {
        assertEquals(id, testWagon.getId());
    }

    @Test
    public void setId() {
        int id = 22;

        testWagon.setId(id);

        assertEquals(id, testWagon.getId());
    }

    @Test
    public void getTrainId() {
        assertEquals(trainId, testWagon.getTrainId());
    }

    @Test
    public void setTrainId() {
        int trainId = 33;

        testWagon.setTrainId(trainId);

        assertEquals(trainId, testWagon.getTrainId());
    }

    @Test
    public void getTypeUA() {
        assertEquals(typeUA, testWagon.getTypeUA());
    }

    @Test
    public void setTypeUA() {
        String typeUA = "Купе";

        testWagon.setTypeUA(typeUA);

        assertEquals(typeUA, testWagon.getTypeUA());
    }

    @Test
    public void getTypeEN() {
        assertEquals(typeEN, testWagon.getTypeEN());
    }

    @Test
    public void setTypeEN() {
        String typeEN = "2nd class";

        testWagon.setTypeEN(typeEN);

        assertEquals(typeEN, testWagon.getTypeEN());
    }

    @Test
    public void getTrain() {
        assertEquals(train, testWagon.getTrain());
    }

    @Test
    public void setTrain() {
        Train train = new Train();

        testWagon.setTrain(train);

        assertEquals(train, testWagon.getTrain());
    }

    @Test
    public void getSeatList() {
        assertEquals(seatList, testWagon.getSeatList());
    }

    @Test
    public void setSeatList() {
        List<Seat> seatList = new ArrayList<>();

        testWagon.setSeatList(seatList);

        assertEquals(seatList, testWagon.getSeatList());
    }

    @Test
    public void addSeatToList() {
        Seat seat = new Seat();

        testWagon.addSeatToList(seat);

        assertEquals(seat, testWagon.getSeatList().get(testWagon.getSeatList().size() - 1));
    }

    @Test
    public void equals() {
        assertEquals(testWagon, testWagon);
    }
}