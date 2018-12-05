package ua.training.model.dao.implement;

import ua.training.model.dao.WagonDao;
import ua.training.model.entity.Wagon;

import java.sql.Connection;
import java.util.List;

public class JDBCWagonDao implements WagonDao {
    private Connection connection;

    public JDBCWagonDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Wagon entity) {

    }

    @Override
    public Wagon findById(Integer id) {
        return null;
    }

    @Override
    public List<Wagon> findAll() {
        return null;
    }

    @Override
    public void update(Wagon entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void close() throws Exception {

    }
}
