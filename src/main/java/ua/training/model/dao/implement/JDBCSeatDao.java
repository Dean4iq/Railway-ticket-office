package ua.training.model.dao.implement;

import ua.training.model.dao.SeatDao;
import ua.training.model.entity.Seat;

import java.sql.Connection;
import java.util.List;

public class JDBCSeatDao implements SeatDao {
    private Connection connection;

    public JDBCSeatDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Seat entity) {

    }

    @Override
    public Seat findById(Integer id) {
        return null;
    }

    @Override
    public List<Seat> findAll() {
        return null;
    }

    @Override
    public void update(Seat entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
