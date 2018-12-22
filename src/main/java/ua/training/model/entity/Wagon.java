package ua.training.model.entity;

public class Wagon {
    private int id;
    private int trainId;
    private String type;

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

    @Override
    public String toString() {
        return "Wagon{" +
                "id=" + id +
                ", trainId=" + trainId +
                ", type='" + type + '\'' +
                '}';
    }
}
