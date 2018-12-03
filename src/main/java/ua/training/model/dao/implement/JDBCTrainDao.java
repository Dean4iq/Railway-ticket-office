package ua.training.model.dao.implement;

import ua.training.model.dao.TrainDao;
import ua.training.model.entity.Train;

import java.sql.Connection;
import java.util.List;

public class JDBCTrainDao implements TrainDao {
    private Connection connection;

    public JDBCTrainDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Train entity) {

    }

    @Override
    public Train findById(int id) {
        return null;
    }

    @Override
    public List<Train> findAll() {
        return null;
    }

    @Override
    public void update(Train entity) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void close() throws Exception {

    }
}
