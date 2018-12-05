package ua.training.model.dao.implement;

import ua.training.model.dao.TicketDao;
import ua.training.model.entity.Ticket;

import java.sql.Connection;
import java.util.List;

public class JDBCTicketDao implements TicketDao {
    private Connection connection;

    public JDBCTicketDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Ticket entity) {

    }

    @Override
    public Ticket findById(Object id) {
        return null;
    }

    @Override
    public List<Ticket> findAll() {
        return null;
    }

    @Override
    public void update(Ticket entity) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void close() throws Exception {

    }
}
