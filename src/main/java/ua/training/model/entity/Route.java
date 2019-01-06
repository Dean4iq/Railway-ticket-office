package ua.training.model.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Route {
    private int trainId;
    private int stationId;
    private Timestamp arrivalTime;
    private Timestamp departureTime;

    private Station station;
    private String localeArrivalTime;
    private String localeDepartureTime;

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public void setLocaleArrivalTime(String localeArrivalTime) {
        this.localeArrivalTime = localeArrivalTime;
    }

    public void setLocaleDepartureTime(String localeDepartureTime) {
        this.localeDepartureTime = localeDepartureTime;
    }

    public String getFormattedArrivalTime() {
        String[] dateTime = localeArrivalTime.split("[*]");
        return dateTime[1];
    }

    public String getFormattedDepartureTime() {
        String[] dateTime = localeDepartureTime.split("[*]");
        return dateTime[1];
    }

    public String getFormattedArrivalDate() {
        String[] dateTime = localeArrivalTime.split("[*]");
        return dateTime[0];
    }

    public String getFormattedDepartureDate() {
        String[] dateTime = localeDepartureTime.split("[*]");
        return dateTime[0];
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        Route route = (Route) object;
        return this.trainId == route.getTrainId()
                && this.stationId == route.getStationId()
                && this.arrivalTime.equals(route.getArrivalTime())
                && this.departureTime.equals(route.getDepartureTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTrainId(), getStationId(), getArrivalTime(), getDepartureTime());
    }

    @Override
    public String toString() {
        return "Route{" +
                "trainId=" + trainId +
                ", stationId=" + stationId +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                '}';
    }
}
