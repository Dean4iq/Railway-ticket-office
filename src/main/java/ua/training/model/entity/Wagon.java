package ua.training.model.entity;

import java.util.List;

public class Wagon {
    private int id;
    private Train train;
    private String type;

    private List<Seat> seats;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Wagon{" +
                "id=" + id +
                ", train=" + train +
                ", type='" + type + '\'' +
                ", seats=" + seats +
                '}';
    }
}
