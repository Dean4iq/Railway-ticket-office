package ua.training.model.entity;

public class Train {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                '}';
    }
}
