package ua.training.util;

import java.util.ResourceBundle;

public class QueryStringGetter {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(PropertiesSource.SQL_QUERIES.source);

    private QueryStringGetter() {
    }

    public static String getQuery(QueryType queryType, TableName table) {
        return resourceBundle.getString(new StringBuilder()
                .append(queryType.getHeader())
                .append(table.toString().toLowerCase())
                .toString());
    }
}
