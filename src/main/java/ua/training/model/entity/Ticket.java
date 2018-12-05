package ua.training.model.entity;

import java.util.Date;

public class Ticket {
    private int id;
    private int userId;
    private Date travelDate;
    private int seatId;
    private int tripInfoId;
    private int cost;
    private int departureStationId;
    private int arrivalStationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getTripInfoId() {
        return tripInfoId;
    }

    public void setTripInfoId(int tripInfoId) {
        this.tripInfoId = tripInfoId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDepartureStationId() {
        return departureStationId;
    }

    public void setDepartureStationId(int departureStationId) {
        this.departureStationId = departureStationId;
    }

    public int getArrivalStationId() {
        return arrivalStationId;
    }

    public void setArrivalStationId(int arrivalStationId) {
        this.arrivalStationId = arrivalStationId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", travelDate=" + travelDate +
                ", seatId=" + seatId +
                ", tripInfoId=" + tripInfoId +
                ", cost=" + cost +
                ", departureStationId=" + departureStationId +
                ", arrivalStationId=" + arrivalStationId +
                '}';
    }
}
