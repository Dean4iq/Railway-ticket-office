package ua.training.model.entity;

public class WagonType {
    private String type;
    private String fullTypeName;
    private int priceFactor;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullTypeName() {
        return fullTypeName;
    }

    public void setFullTypeName(String fullTypeName) {
        this.fullTypeName = fullTypeName;
    }

    public int getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(int priceFactor) {
        this.priceFactor = priceFactor;
    }

    @Override
    public String toString() {
        return "WagonType{" +
                "type='" + type + '\'' +
                ", fullTypeName='" + fullTypeName + '\'' +
                ", priceFactor=" + priceFactor +
                '}';
    }
}
