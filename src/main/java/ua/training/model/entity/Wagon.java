package ua.training.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Wagon {
    private int id;
    private int trainId;
    private String type;

    private Train train;
    private List<Seat> seatList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public void addSeatToList(Seat seat){
        seatList.add(seat);
    }

    @Override
    public String toString() {
        return "Wagon{" +
                "id=" + id +
                ", trainId=" + trainId +
                ", type='" + type + '\'' +
                '}';
    }
}
