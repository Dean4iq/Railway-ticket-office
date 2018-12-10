package ua.training.model.entity;

import java.sql.Date;

public class Ticket {
    private int id;
    private String userLogin;
    private Date travelDate;
    private int seatId;
    private int trainId;
    private int cost;
    private int departureStationId;
    private int arrivalStationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
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
                ", userLogin=" + userLogin +
                ", travelDate=" + travelDate +
                ", seatId=" + seatId +
                ", trainId=" + trainId +
                ", cost=" + cost +
                ", departureStationId=" + departureStationId +
                ", arrivalStationId=" + arrivalStationId +
                '}';
    }
}
