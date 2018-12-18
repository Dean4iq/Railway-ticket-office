package ua.training.util;

public enum PropertiesSource {
    SQL_QUERIES("sql_queries"),
    LOCALIZATION_STRINGS("messages");

    String source;

    PropertiesSource(String source) {
        this.source = source;
    }
}
