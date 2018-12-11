package ua.training.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class LocalizationGetter {
    private static final Logger log = LogManager.getLogger(LocalizationGetter.class);

    public Map<String, String> getLocalization(String lang) {
        Map<String, String> localeKeys = new HashMap<>();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", new Locale(lang));

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/finalproject", "user", "pass");
             PreparedStatement preparedStatement =
                     connection.prepareStatement(resourceBundle.getString("sql.locale.query"));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Node node = extractFromResultSet(resultSet);
                localeKeys.put(node.key, node.value);
            }
        } catch (SQLException e) {
            log.debug("Error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return localeKeys;
    }

    private Node extractFromResultSet(ResultSet resultSet) throws SQLException{
        String key = resultSet.getString("key_value");
        String value = resultSet.getString("locale_value");
        return new Node(key, value);
    }

    private class Node {
        String key;
        String value;

        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
