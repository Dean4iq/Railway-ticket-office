package ua.training.model.dao.implement;

import ua.training.model.dao.TrainDao;
import ua.training.model.entity.Train;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCTrainDao implements TrainDao {
    private Connection connection;

    public JDBCTrainDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Train train) {
        try (PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO train (tr_id)" +
                        " VALUES ?")) {
            connection.setAutoCommit(false);

            ps.setInt(1, train.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Train findById(Integer id) {
        Train train = new Train();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT * FROM train LEFT JOIN wagon ON Train_tr_id=tr_id WHERE tr_id = ?")) {
            preparedStatement.setInt(1, id);

            train = extractFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return train;
    }

    static Train extractFromResultSet(ResultSet resultSet) throws SQLException {
        Train train = new Train();

        train.setId(resultSet.getInt("tr_id"));

        return train;
    }

    @Override
    public List<Train> findAll() {
        return null;
    }

    @Override
    public void update(Train entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void close() throws Exception {

    }
}
