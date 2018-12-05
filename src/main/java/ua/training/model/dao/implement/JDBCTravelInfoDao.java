package ua.training.model.dao.implement;

import ua.training.model.dao.TravelInfoDao;
import ua.training.model.entity.TravelInfo;

import java.sql.Connection;
import java.util.List;

public class JDBCTravelInfoDao implements TravelInfoDao {
    private Connection connection;

    public JDBCTravelInfoDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(TravelInfo entity) {

    }

    @Override
    public TravelInfo findById(Integer id) {
        return null;
    }

    @Override
    public List<TravelInfo> findAll() {
        return null;
    }

    @Override
    public void update(TravelInfo entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void close() throws Exception {

    }
}
