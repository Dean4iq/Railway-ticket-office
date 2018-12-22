package ua.training.model.dao.daoimplementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.WagonDao;
import ua.training.model.entity.Wagon;
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

public class JDBCWagonDao implements WagonDao {
    private static final Logger log = LogManager.getLogger(JDBCWagonDao.class);
    private static final TableName tableName = TableName.WAGON;

    private Connection connection;

    public JDBCWagonDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCWagonDao constructor()");
    }

    @Override
    public void create(Wagon wagon) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            preparedStatement.setInt(1, wagon.getTrainId());
            preparedStatement.setString(2, wagon.getType());

            preparedStatement.execute();
            log.debug("JDBCWagonDao create()");
        } catch (SQLException e) {
            log.debug("JDBCWagonDao create() failed: " + wagon.toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Wagon findById(Integer id) {
        Wagon wagon = new Wagon();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                wagon = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.debug("JDBCWagonDao findById() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCWagonDao findById()");
        return wagon;
    }

    static Wagon extractFromResultSet(ResultSet resultSet) throws SQLException {
        Wagon wagon = new Wagon();

        wagon.setId(resultSet.getInt("w_id"));
        wagon.setTrainId(resultSet.getInt("Train_t_id"));
        wagon.setType(resultSet.getString("type"));

        log.debug("JDBCWagonDao extractFromResultSet(): " + wagon.toString());
        return wagon;
    }

    @Override
    public List<Wagon> findAll() {
        List<Wagon> wagonList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                wagonList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.debug("JDBCWagonDao findAll() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCWagonDao findAll()");
        return wagonList;
    }

    @Override
    public void update(Wagon wagon) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
            preparedStatement.setInt(1, wagon.getTrainId());
            preparedStatement.setString(2, wagon.getType());
            preparedStatement.setInt(3, wagon.getId());

            preparedStatement.executeUpdate();
            log.debug("JDBCWagonDao update()");
        } catch (SQLException e) {
            log.debug("JDBCWagonDao update() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Wagon wagon) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setInt(1, wagon.getId());

            preparedStatement.execute();
            log.debug("JDBCWagonDao delete()");
        } catch (SQLException e) {
            log.debug("JDBCWagonDao delete() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("JDBCWagonDao close()");
        connection.close();
    }
}
