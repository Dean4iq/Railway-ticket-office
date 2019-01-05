package ua.training.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 4710159281959669434L;

    private int id;
    private String userLogin;
    private Date travelDate;
    private int seatId;
    private int trainId;
    private int cost;
    private int departureStationId;
    private int arrivalStationId;

    private int wagonId;

    private transient Seat seat;
    private transient Train train;
    private transient Station departureStation;
    private transient Station arrivalStation;

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

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getWagonId() {
        return wagonId;
    }

    public void setWagonId(int wagonId) {
        this.wagonId = wagonId;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(Station departureStation) {
        this.departureStation = departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(Station arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        Ticket ticket = (Ticket) object;
        return getId() == ticket.getId()
                && getSeatId() == ticket.getSeatId()
                && getTrainId() == ticket.getTrainId()
                && getCost() == ticket.getCost()
                && getDepartureStationId() == ticket.getDepartureStationId()
                && getArrivalStationId() == ticket.getArrivalStationId()
                && Objects.equals(getUserLogin(), ticket.getUserLogin())
                && Objects.equals(getTravelDate(), ticket.getTravelDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserLogin(), getTravelDate(),
                getSeatId(), getTrainId(), getCost(), getDepartureStationId(),
                getArrivalStationId());
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
