package ua.training.util;

public enum QueryType {
    INSERT("insert"),
    FIND("find"),
    SELECT("select"),
    UPDATE("update"),
    DELETE("delete"),
    FIND_BY_TRAIN("findByTrain"),
    GET("get");

    String header;

    QueryType(String header){
        this.header = new StringBuilder().append("query.").append(header).append(".").toString();
    }

    public String getHeader() {
        return header;
    }
}
