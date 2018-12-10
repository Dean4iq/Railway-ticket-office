package ua.training.util;

import java.util.ResourceBundle;

public class QueryStringGetter {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("sql_queries_en");

    private QueryStringGetter() {
    }

    public static String getQuery(QueryType queryType, TableName table) {
        return resourceBundle.getString(new StringBuilder()
                .append(queryType.getHeader())
                .append(".")
                .append(table.toString().toLowerCase())
                .toString());
    }
}
