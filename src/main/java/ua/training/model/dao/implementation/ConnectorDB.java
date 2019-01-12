package ua.training.model.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public enum ConnectorDB {
    INSTANCE;

    private final Logger log = LogManager.getLogger(ConnectorDB.class);
    private final int poolSize = 10;
    private List<Connection> connectionPool;
    private List<Connection> connectionsInUse;

    ConnectorDB() {
        connectionPool = new Vector<>(poolSize);
        connectionsInUse = new ArrayList<>();

        try {
            for (int i = 0; i < poolSize; i++) {
                connectionPool.add(createConnection());
            }
        } catch (SQLException e) {
            log.error("Exception while creating pool: {}", e);
        }
    }

    public synchronized Connection getConnection() {
        while (connectionPool.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error("Exception while waiting for pool: {}", e);
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        connectionsInUse.add(connection);

        return connection;
    }

    public synchronized boolean returnConnectionToPool(Connection connection) {
        connectionPool.add(connection);
        notifyAll();

        return connectionsInUse.remove(connection);
    }

    public void shutDown() {
        connectionPool.forEach(conn -> {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("Exception while closing connection in pool: {}", e);
            }
        });

        connectionsInUse.forEach(conn -> {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("Exception while closing connection in use: {}", e);
            }
        });
    }

    private Connection createConnection() throws SQLException {
        ResourceBundle resource = ResourceBundle.getBundle("database");

        String url = resource.getString("db.url");
        String user = resource.getString("db.user");
        String pass = resource.getString("db.password");

        return DriverManager.getConnection(url, user, pass);

    }


}
