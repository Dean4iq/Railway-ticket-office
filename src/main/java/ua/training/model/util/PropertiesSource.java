package ua.training.model.util;

public enum PropertiesSource {
    SQL_QUERIES("sql_queries"),
    LOCALIZATION_STRINGS("messages"),
    REGEX_STRINGS("regex");

    String source;

    PropertiesSource(String source) {
        this.source = source;
    }
}
