package ua.training.model.entity;

public class Train {
    private int id;
    private int cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", cost=" + cost +
                '}';
    }
}
