package ua.training.model.entity;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

public class RouteTest {
    private Route testRoute;

    private int trainId = 34;
    private int stationId = 11;
    private Timestamp arrivalTime = new Timestamp(new Date(2335423).getTime());
    private Timestamp departureTime = new Timestamp(new Date(123432).getTime());

    private Station station = new Station();
    private String localeArrivalTime = "2019-01-01*12:58";
    private String localeDepartureTime = "2019-01-01*23:05";

    @Before
    public void setUp() {
        testRoute = new Route();
        station.setId(23);

        testRoute.setTrainId(trainId);
        testRoute.setStationId(stationId);
        testRoute.setArrivalTime(arrivalTime);
        testRoute.setDepartureTime(departureTime);
        testRoute.setLocaleArrivalTime(localeArrivalTime);
        testRoute.setLocaleDepartureTime(localeDepartureTime);

        testRoute.setStation(station);
    }

    @Test
    public void getTrainId() {
        assertEquals(trainId, testRoute.getTrainId());
    }

    @Test
    public void setTrainId() {
        int trainId = 111;

        testRoute.setTrainId(trainId);

        assertEquals(trainId, testRoute.getTrainId());
    }

    @Test
    public void getStationId() {
        assertEquals(stationId, testRoute.getStationId());
    }

    @Test
    public void setStationId() {
        int stationId = 121;

        testRoute.setStationId(stationId);

        assertEquals(stationId, testRoute.getStationId());
    }

    @Test
    public void getArrivalTime() {
        assertEquals(arrivalTime, testRoute.getArrivalTime());
    }

    @Test
    public void setArrivalTime() {
        Timestamp arrivalTime = new Timestamp(new Date().getTime());

        testRoute.setArrivalTime(arrivalTime);

        assertEquals(arrivalTime, testRoute.getArrivalTime());
    }

    @Test
    public void getDepartureTime() {
        assertEquals(departureTime, testRoute.getDepartureTime());
    }

    @Test
    public void setDepartureTime() {
        Timestamp departureTime = new Timestamp(new Date().getTime());

        testRoute.setDepartureTime(departureTime);

        assertEquals(departureTime, testRoute.getDepartureTime());
    }

    @Test
    public void getFormattedArrivalTime() {
        assertEquals(localeArrivalTime.split("[*]")[1], testRoute.getFormattedArrivalTime());
    }

    @Test
    public void getFormattedDepartureTime() {
        assertEquals(localeDepartureTime.split("[*]")[1], testRoute.getFormattedDepartureTime());
    }

    @Test
    public void getFormattedArrivalDate() {
        assertEquals(localeArrivalTime.split("[*]")[0], testRoute.getFormattedArrivalDate());
    }

    @Test
    public void getFormattedDepartureDate() {
        assertEquals(localeDepartureTime.split("[*]")[0], testRoute.getFormattedDepartureDate());
    }

    @Test
    public void getStation() {
        assertEquals(station, testRoute.getStation());
    }

    @Test
    public void setStation() {
        Station station = new Station();
        station.setId(333);

        testRoute.setStation(station);

        assertEquals(station, testRoute.getStation());
    }

    @Test
    public void equals() {
        Route routeToEqual = new Route();

        routeToEqual.setTrainId(trainId);
        routeToEqual.setStationId(stationId);
        routeToEqual.setArrivalTime(arrivalTime);
        routeToEqual.setDepartureTime(departureTime);
        routeToEqual.setLocaleArrivalTime(localeArrivalTime);
        routeToEqual.setLocaleDepartureTime(localeDepartureTime);

        routeToEqual.setStation(station);

        assertTrue(testRoute.equals(routeToEqual));
    }

    @Test
    public void setLocaleArrivalTime() {
        String localeArrivalTime = "2019-01-11*16:55";

        testRoute.setLocaleArrivalTime(localeArrivalTime);

        assertEquals(localeArrivalTime,
                testRoute.getFormattedArrivalDate()+"*"+testRoute.getFormattedArrivalTime());
    }

    @Test
    public void setLocaleDepartureTime() {
        String localeDepartureTime = "2019-01-01*23:55";

        testRoute.setLocaleDepartureTime(localeDepartureTime);

        assertEquals(localeDepartureTime,
                testRoute.getFormattedDepartureDate()+"*"+testRoute.getFormattedDepartureTime());
    }
}