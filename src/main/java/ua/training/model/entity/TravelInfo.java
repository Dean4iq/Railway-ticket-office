package ua.training.model.entity;

public class TravelInfo {
    private int id;
    private String days;
    private Train train;

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

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @Override
    public String toString() {
        return "TravelInfo{" +
                "id=" + id +
                ", days='" + days + '\'' +
                ", train=" + train +
                '}';
    }
}
