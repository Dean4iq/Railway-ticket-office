package ua.training.model.entity;

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
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", wagonId=" + wagonId +
                ", occupied=" + occupied +
                '}';
    }
}
