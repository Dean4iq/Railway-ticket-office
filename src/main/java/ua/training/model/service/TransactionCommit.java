package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

class TransactionCommit {
    private static final Logger LOG = LogManager.getLogger(TransactionCommit.class);

    void commitAndClose(Connection connection) {
        try {
            if (!connection.isClosed()) {
                connection.commit();
                connection.close();

                LOG.debug("Commit and close");
            }
        } catch (SQLException exception) {
            LOG.error("Transaction commit problem occurred: ", exception);
            rollbackAndClose(connection);
        }
    }

    void rollbackAndClose(Connection connection) {
        try {
            if (!connection.isClosed()) {
                connection.rollback();
                connection.close();

                LOG.debug("Rollback and close");
            }
        } catch (SQLException exception) {
            LOG.error("Transaction problem occurred during rollback: ", exception);
        }
    }
}
