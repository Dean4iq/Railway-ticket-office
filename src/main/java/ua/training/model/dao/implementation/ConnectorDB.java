package ua.training.model.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public enum ConnectorDB {
    INSTANCE;

    private final Logger log = LogManager.getLogger(ConnectorDB.class);
    private final int poolSize = 10;
    private List<Connection> connectionPool;

    ConnectorDB() {
        connectionPool = new Vector<>(poolSize);

        try {
            for (int i = 0; i < poolSize; i++) {
                connectionPool.add(createConnection());
            }
        } catch (SQLException e) {
            log.error("Exception while creating pool: {}", e);
        }
    }

    public synchronized Connection getConnection() {
        while (connectionPool.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                log.error("Exception while waiting for pool: {}", e);
            }
        }

        return connectionPool.remove(connectionPool.size() - 1);
    }

    public synchronized void returnConnectionToPool(Connection connection) {
        connectionPool.add(connection);
        notifyAll();
    }

    private Connection createConnection() throws SQLException {
        ResourceBundle resource = ResourceBundle.getBundle("database");

        String url = resource.getString("db.url");
        String user = resource.getString("db.user");
        String pass = resource.getString("db.password");

        return DriverManager.getConnection(url, user, pass);

    }
}
