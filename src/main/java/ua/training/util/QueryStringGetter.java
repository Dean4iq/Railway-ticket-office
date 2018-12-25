package ua.training.util;

import java.util.ResourceBundle;

public final class QueryStringGetter {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(PropertiesSource.SQL_QUERIES.source);

    private QueryStringGetter() {
    }

    public static String getQuery(QueryType queryType, TableName table) {
        return RESOURCE_BUNDLE.getString(new StringBuilder()
                .append(queryType.getHeader())
                .append(table.toString().toLowerCase())
                .toString());
    }
}
