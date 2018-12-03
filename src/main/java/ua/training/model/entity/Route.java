package ua.training.model.entity;

import java.sql.Time;

public class Route {
    private Train train;
    private int positionId;
    private Time arrivalTime;
    private Time departureTime;
    private Station station;

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getPosition() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return "Route{" +
                "train=" + train +
                ", positionId=" + positionId +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", station=" + station +
                '}';
    }
}
