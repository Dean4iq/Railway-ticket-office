package ua.training.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void addSeatToList(Seat seat) {
        seatList.add(seat);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        Wagon wagon = (Wagon) object;
        return getId() == wagon.getId()
                && getTrainId() == wagon.getTrainId()
                && getType().equals(wagon.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTrainId(), getType());
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
