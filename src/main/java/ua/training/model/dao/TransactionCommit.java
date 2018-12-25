package ua.training.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionCommit {
    private static final Logger LOG = LogManager.getLogger(TransactionCommit.class);

    public void commitAndClose(Connection connection) {
        try {
            if (!connection.isClosed()) {
                connection.commit();
                connection.close();
            }
        } catch (SQLException exception) {
            LOG.error("Transaction commit problem occurred: ", exception);
            rollbackAndClose(connection);
        }
    }

    public void rollbackAndClose(Connection connection) {
        try {
            if (!connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (SQLException exception) {
            LOG.error("Transaction problem occurred during rollback: ", exception);
        }
    }
}
