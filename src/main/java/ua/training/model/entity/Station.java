package ua.training.model.entity;

import java.util.Objects;

public class Station {
    private int id;
    private String nameEN;
    private String nameUA;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String name) {
        this.nameEN = name;
    }

    public String getNameUA() {
        return nameUA;
    }

    public void setNameUA(String nameUA) {
        this.nameUA = nameUA;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        Station station = (Station) object;
        return getId() == station.getId()
                && getNameEN().equals(station.getNameEN())
                && getNameUA().equals(station.getNameUA());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNameEN(), getNameUA());
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", nameEN='" + nameEN + '\'' +
                ", nameUA='" + nameUA + '\'' +
                '}';
    }
}
