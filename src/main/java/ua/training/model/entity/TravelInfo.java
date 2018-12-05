package ua.training.model.entity;

public class TravelInfo {
    private int id;
    private String days;
    private int trainId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    @Override
    public String toString() {
        return "TravelInfo{" +
                "id=" + id +
                ", days='" + days + '\'' +
                ", trainId=" + trainId +
                '}';
    }
}
