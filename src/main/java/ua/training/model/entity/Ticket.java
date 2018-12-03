package ua.training.model.entity;

import java.util.Date;

public class Ticket {
    private int id;
    private Date travelDate;
    private Wagon wagonNumber;
    private Seat seatNumber;

    private User user;
    private TravelInfo travelInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public Wagon getWagonNumber() {
        return wagonNumber;
    }

    public void setWagonNumber(Wagon wagonNumber) {
        this.wagonNumber = wagonNumber;
    }

    public Seat getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Seat seatNumber) {
        this.seatNumber = seatNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TravelInfo getTravelInfo() {
        return travelInfo;
    }

    public void setTravelInfo(TravelInfo travelInfo) {
        this.travelInfo = travelInfo;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", travelDate=" + travelDate +
                ", wagonNumber=" + wagonNumber +
                ", seatNumber=" + seatNumber +
                ", user=" + user +
                ", travelInfo=" + travelInfo +
                '}';
    }
}
