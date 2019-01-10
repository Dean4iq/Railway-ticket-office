package ua.training.model.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.WagonDao;
import ua.training.model.entity.Seat;
import ua.training.model.entity.Train;
import ua.training.model.entity.Wagon;
import ua.training.model.util.QueryStringGetter;
import ua.training.model.util.QueryType;
import ua.training.model.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCWagonDao implements WagonDao {
    private static final Logger LOG = LogManager.getLogger(JDBCWagonDao.class);
    private static final TableName TABLE_NAME = TableName.WAGON;

    private Connection connection;

    public JDBCWagonDao(Connection connection) {
        this.connection = connection;
        LOG.debug("JDBCWagonDao constructor()");
    }

    @Override
    public void create(Wagon wagon) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, TABLE_NAME))) {
            preparedStatement.setInt(1, wagon.getTrainId());
            preparedStatement.setString(2, wagon.getTypeUA());
            preparedStatement.setString(3, wagon.getTypeEN());

            preparedStatement.executeUpdate();
            LOG.debug("JDBCWagonDao create()");
        } catch (SQLException e) {
            LOG.debug("JDBCWagonDao create() failed: " + wagon.toString());
            LOG.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Wagon findById(Integer id) {
        Wagon wagon = new Wagon();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, TABLE_NAME))) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                wagon = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOG.debug("JDBCWagonDao findById() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        LOG.debug("JDBCWagonDao findById()");
        return wagon;
    }

    public List<Wagon> findByTrainId(Integer trainId) {
        List<Wagon> wagonList = new ArrayList<>();
        Map<Integer, Wagon> wagonMap = new HashMap<>();
        Map<Integer, Train> trainMap = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND_BY_TRAIN, TABLE_NAME))) {
            preparedStatement.setInt(1, trainId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Wagon wagon = extractFromResultSet(resultSet);
                Seat seat = JDBCSeatDao.extractFromResultSet(resultSet);
                Train train = JDBCTrainDao.extractFromResultSet(resultSet);

                wagon = makeUniqueWagon(wagonMap, wagon);
                train= makeUniqueTrain(trainMap, train);

                seat.setWagon(wagon);
                wagon.addSeatToList(seat);
                wagon.setTrain(train);
            }

            wagonList = new ArrayList<>(wagonMap.values());
        } catch (SQLException e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
        return wagonList;
    }

    private Wagon makeUniqueWagon(Map<Integer, Wagon> wagonMap, Wagon wagon) {
        wagonMap.putIfAbsent(wagon.getId(), wagon);

        return wagonMap.get(wagon.getId());
    }

    private Train makeUniqueTrain(Map<Integer, Train> trainMap, Train train) {
        trainMap.putIfAbsent(train.getId(), train);

        return trainMap.get(train.getId());
    }

    static Wagon extractFromResultSet(ResultSet resultSet) throws SQLException {
        Wagon wagon = new Wagon();

        wagon.setId(resultSet.getInt("w_id"));
        wagon.setTrainId(resultSet.getInt("Train_t_id"));
        wagon.setTypeUA(resultSet.getString("typeUA"));
        wagon.setTypeEN(resultSet.getString("typeEN"));

        LOG.debug("JDBCWagonDao extractFromResultSet(): " + wagon.toString());
        return wagon;
    }

    @Override
    public List<Wagon> findAll() {
        List<Wagon> wagonList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, TABLE_NAME));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                wagonList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOG.debug("JDBCWagonDao findAll() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        LOG.debug("JDBCWagonDao findAll()");
        return wagonList;
    }

    @Override
    public void update(Wagon wagon) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.UPDATE, TABLE_NAME))) {
            preparedStatement.setInt(1, wagon.getTrainId());
            preparedStatement.setString(2, wagon.getTypeUA());
            preparedStatement.setString(3, wagon.getTypeEN());
            preparedStatement.setInt(4, wagon.getId());

            preparedStatement.executeUpdate();
            LOG.debug("JDBCWagonDao update()");
        } catch (SQLException e) {
            LOG.debug("JDBCWagonDao update() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Wagon wagon) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, TABLE_NAME))) {
            preparedStatement.setInt(1, wagon.getId());

            preparedStatement.executeUpdate();
            LOG.debug("JDBCWagonDao delete()");
        } catch (SQLException e) {
            LOG.debug("JDBCWagonDao delete() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        LOG.debug("JDBCWagonDao close()");
        connection.close();
    }
}
