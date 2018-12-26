package ua.training.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private int id;
    private int cost;

    private List<Route> routeList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Route getDepartureRoute() {
        return (routeList.isEmpty()) ? null : routeList.get(0);
    }

    public Route getArrivalRoute() {
        return (routeList.isEmpty()) ? null : routeList.get(routeList.size() - 1);
    }

    public List<Route> getStations() {
        return routeList;
    }

    public void addRoute(Route route) {
        routeList.add(route);
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", cost=" + cost +
                '}';
    }
}
