package ua.training.model.entity;

import java.util.Objects;

public class Seat {
    private int id;
    private int wagonId;
    private boolean occupied;

    private Wagon wagon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWagonId() {
        return wagonId;
    }

    public void setWagonId(int wagonId) {
        this.wagonId = wagonId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Wagon getWagon() {
        return wagon;
    }

    public void setWagon(Wagon wagon) {
        this.wagon = wagon;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object){
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        Seat seat = (Seat) object;
        return getId() == seat.getId()
                && getWagonId() == seat.getWagonId()
                && isOccupied() == seat.isOccupied()
                && getWagon().equals(seat.getWagon());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getWagonId(), isOccupied(), getWagon());
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", wagonId=" + wagonId +
                ", occupied=" + occupied +
                '}';
    }
}
