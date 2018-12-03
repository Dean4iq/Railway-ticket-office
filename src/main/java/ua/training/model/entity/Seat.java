package ua.training.model.entity;

public class Seat {
    private int id;
    private Wagon wagon;
    private boolean occupied;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wagon getWagon() {
        return wagon;
    }

    public void setWagon(Wagon wagon) {
        this.wagon = wagon;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", wagon=" + wagon +
                ", occupied=" + occupied +
                '}';
    }
}
