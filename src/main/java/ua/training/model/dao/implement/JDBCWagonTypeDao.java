package ua.training.model.dao.implement;

import ua.training.model.dao.WagonTypeDao;
import ua.training.model.entity.WagonType;

import java.sql.Connection;
import java.util.List;

public class JDBCWagonTypeDao implements WagonTypeDao {
    private Connection connection;

    public JDBCWagonTypeDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(WagonType entity) {

    }

    @Override
    public WagonType findById(Integer id) {
        return null;
    }

    @Override
    public List<WagonType> findAll() {
        return null;
    }

    @Override
    public void update(WagonType entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void close() throws Exception {

    }
}
