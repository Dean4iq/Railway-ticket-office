package ua.training.model.entity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TrainTest {
    private Train testTrain;

    private int id = 2;
    private int cost = 450;

    private String localeCost = "450,00";

    private List<Route> routeList = new ArrayList<>();
    private List<Route> userRouteList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        testTrain = new Train();

        routeList.add(new Route());
        routeList.add(new Route());

        userRouteList.addAll(routeList);

        testTrain.setId(id);
        testTrain.setCost(cost);
        testTrain.setLocaleCost(localeCost);
        testTrain.setRouteList(routeList);
        testTrain.setUserRouteList(userRouteList);
    }

    @Test
    public void getId() {
        assertEquals(id, testTrain.getId());
    }

    @Test
    public void setId() {
        int id = 5;

        testTrain.setId(id);

        assertEquals(id, testTrain.getId());
    }

    @Test
    public void getCost() {
        assertEquals(cost, testTrain.getCost());
    }

    @Test
    public void setCost() {
        int cost = 500;

        testTrain.setCost(cost);

        assertEquals(cost, testTrain.getCost());
    }

    @Test
    public void getDepartureRoute() {
        assertEquals(routeList.get(0), testTrain.getDepartureRoute());
    }

    @Test
    public void getArrivalRoute() {
        assertEquals(routeList.get(routeList.size() - 1), testTrain.getArrivalRoute());
    }

    @Test
    public void addRoute() {
        Route route = new Route();

        testTrain.addRoute(route);

        assertEquals(route, testTrain.getArrivalRoute());
    }

    @Test
    public void getRouteList() {
        assertEquals(routeList, testTrain.getRouteList());
    }

    @Test
    public void getUserRouteList() {
        assertEquals(userRouteList, testTrain.getUserRouteList());
    }

    @Test
    public void setUserRouteList() {
        List<Route> userRouteList = new ArrayList<>();

        testTrain.setUserRouteList(userRouteList);

        assertEquals(userRouteList, testTrain.getUserRouteList());
    }

    @Test
    public void getUserDepartureRoute() {
        assertEquals(userRouteList.get(0), testTrain.getUserDepartureRoute());
    }

    @Test
    public void getUserArrivalRoute() {
        assertEquals(userRouteList.get(userRouteList.size()-1), testTrain.getUserArrivalRoute());
    }

    @Test
    public void getLocaleCost() {
        assertEquals(localeCost, testTrain.getLocaleCost());
    }

    @Test
    public void setLocaleCost() {
        String localeCost = "245.56";

        testTrain.setLocaleCost(localeCost);

        assertEquals(localeCost, testTrain.getLocaleCost());
    }

    @Test
    public void setRouteList() {
        List<Route> routeList = new ArrayList<>();

        testTrain.setRouteList(routeList);

        assertEquals(routeList, testTrain.getRouteList());
    }

    @Test
    public void equals() {
        Train train = testTrain;

        assertEquals(train, testTrain);
    }
}