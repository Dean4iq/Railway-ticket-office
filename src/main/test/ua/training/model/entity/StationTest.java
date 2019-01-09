package ua.training.model.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StationTest {
    private Station testStation;

    private int id = 45;
    private String nameEN = "Kyiv";
    private String nameUA = "Київ";

    @Before
    public void setUp() throws Exception {
        testStation = new Station();

        testStation.setId(id);
        testStation.setNameEN(nameEN);
        testStation.setNameUA(nameUA);
    }

    @Test
    public void getId() {
        assertEquals(id, testStation.getId());
    }

    @Test
    public void setId() {
        int id = 22;

        testStation.setId(id);

        assertEquals(id, testStation.getId());
    }

    @Test
    public void getNameEN() {
        assertEquals(nameEN, testStation.getNameEN());
    }

    @Test
    public void setNameEN() {
        String nameEN = "Odesa";

        testStation.setNameEN(nameEN);

        assertEquals(nameEN, testStation.getNameEN());
    }

    @Test
    public void getNameUA() {
        assertEquals(nameUA, testStation.getNameUA());
    }

    @Test
    public void setNameUA() {
        String nameUA = "Одеса";

        testStation.setNameUA(nameUA);

        assertEquals(nameUA, testStation.getNameUA());
    }

    @Test
    public void notEquals() {
        assertNotEquals(new Object(), testStation);
    }

    @Test
    public void equals() {
        Station station = new Station();

        station.setId(id);
        station.setNameEN(nameEN);
        station.setNameUA(nameUA);

        assertEquals(station, testStation);
    }
}