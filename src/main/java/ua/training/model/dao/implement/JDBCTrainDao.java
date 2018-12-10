package ua.training.model.dao.implement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.TrainDao;
import ua.training.model.entity.Train;
import ua.training.util.QueryStringGetter;
import ua.training.util.QueryType;
import ua.training.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDBCTrainDao implements TrainDao {
    private static final Logger log = LogManager.getLogger(JDBCTrainDao.class);
    private static final TableName tableName = TableName.TRAIN;

    private Connection connection;

    public JDBCTrainDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCTrainDao constructor()");
    }

    @Override
    public void create(Train train) {
        try (PreparedStatement ps = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            ps.setInt(1, train.getCost());
            ps.execute();

            log.debug("JDBCTrainDao create()");
        } catch (SQLException e) {
            log.debug("JDBCTrainDao create() failed: " + train.toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Train findById(Integer id) {
        Train train = new Train();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                train = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.debug("JDBCTrainDao findById() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCTrainDao findById()");
        return train;
    }

    static Train extractFromResultSet(ResultSet resultSet) throws SQLException {
        Train train = new Train();

        train.setId(resultSet.getInt("tr_id"));
        train.setCost(resultSet.getInt("cost"));

        log.debug("JDBCTrainDao extractFromResultSet(): " + train.toString());
        return train;
    }

    @Override
    public List<Train> findAll() {
        List<Train> trainList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                trainList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.debug("JDBCTrainDao findAll() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCTrainDao findAll()");
        return trainList;
    }

    @Override
    public void update(Train train) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
            preparedStatement.setInt(1, train.getCost());
            preparedStatement.setInt(2, train.getId());

            preparedStatement.executeUpdate();
            log.debug("JDBCTrainDao update()");
        } catch (SQLException e) {
            log.debug("JDBCTrainDao update() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Train train) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setInt(1, train.getId());

            preparedStatement.execute();
            log.debug("JDBCTrainDao delete()");
        } catch (SQLException e) {
            log.debug("JDBCTrainDao delete() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("JDBCTrainDao close()");
        connection.close();
    }
}
