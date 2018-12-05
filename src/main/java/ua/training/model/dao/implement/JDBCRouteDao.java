package ua.training.model.dao.implement;

import ua.training.model.dao.RouteDao;
import ua.training.model.entity.Route;

import java.sql.Connection;
import java.util.List;

public class JDBCRouteDao implements RouteDao {
    private Connection connection;

    public JDBCRouteDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Route entity) {

    }

    @Override
    public Route findById(Object id) {
        return null;
    }

    @Override
    public List<Route> findAll() {
        return null;
    }

    @Override
    public void update(Route entity) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void close() throws Exception {

    }
}
