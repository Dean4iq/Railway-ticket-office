package ua.training.model.entity;

import java.util.List;

public class Train {
    private int id;
    private List<Wagon> wagons;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Wagon> getWagons() {
        return wagons;
    }

    public void setWagons(List<Wagon> wagons) {
        this.wagons = wagons;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", wagons=" + wagons +
                '}';
    }
}
