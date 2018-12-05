package ua.training.model.dao.implement;

import ua.training.model.dao.StationDao;
import ua.training.model.entity.Station;

import java.sql.Connection;
import java.util.List;

public class JDBCStationDao implements StationDao {
    private Connection connection;

    public JDBCStationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Station entity) {

    }

    @Override
    public Station findById(Object id) {
        return null;
    }

    @Override
    public List<Station> findAll() {
        return null;
    }

    @Override
    public void update(Station entity) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void close() throws Exception {

    }
}
