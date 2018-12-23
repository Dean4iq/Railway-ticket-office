package ua.training.model.dao.daoimplementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.SeatDao;
import ua.training.model.entity.Seat;
import ua.training.model.entity.Train;
import ua.training.model.entity.Wagon;
import ua.training.util.QueryStringGetter;
import ua.training.util.QueryType;
import ua.training.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCSeatDao implements SeatDao {
    private static final Logger LOG = LogManager.getLogger(JDBCRouteDao.class);
    private static final TableName TABLE_NAME = TableName.SEAT;

    private Connection connection;

    public JDBCSeatDao(Connection connection) {
        this.connection = connection;
        LOG.debug("JDBCSeatDao constructor()");
    }

    @Override
    public void create(Seat seat) {
        try (PreparedStatement ps = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, TABLE_NAME))) {
            ps.setInt(1, seat.getWagonId());
            ps.execute();

            LOG.debug("JDBCSeatDao create()");
        } catch (SQLException e) {
            LOG.debug("JDBCSeatDao create() failed: " + seat.toString());
            LOG.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Seat findById(Integer id) {
        Seat seat = new Seat();

        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(QueryStringGetter.getQuery(QueryType.FIND, TABLE_NAME))) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                seat = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOG.debug("JDBCSeatDao findById() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        LOG.debug("JDBCSeatDao findById()");
        return seat;
    }

    public List<Seat> findByTrainId(Integer trainId) {
        List<Seat> seatList = new ArrayList<>();
        Map<Integer, Wagon> wagonMap = new HashMap<>();
        Map<Integer, Train> trainMap = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND_BY_TRAIN, TABLE_NAME))) {
            preparedStatement.setInt(1, trainId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Seat seat = extractFromResultSet(resultSet);
                Wagon wagon = JDBCWagonDao.extractFromResultSet(resultSet);
                Train train = JDBCTrainDao.extractFromResultSet(resultSet);

                seat.setWagon(makeUniqueWagon(wagonMap, wagon));
                seat.getWagon().setTrain(makeUniqueTrain(trainMap, train));

                seatList.add(seat);
            }
        } catch (SQLException e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
        return seatList;
    }

    private Wagon makeUniqueWagon(Map<Integer, Wagon> wagonMap, Wagon wagon) {
        wagonMap.putIfAbsent(wagon.getId(), wagon);

        return wagonMap.get(wagon.getId());
    }

    private Train makeUniqueTrain(Map<Integer, Train> trainMap, Train train) {
        trainMap.putIfAbsent(train.getId(), train);

        return trainMap.get(train.getId());
    }

    static Seat extractFromResultSet(ResultSet resultSet) throws SQLException {
        Seat seat = new Seat();

        seat.setId(resultSet.getInt("s_id"));
        seat.setWagonId(resultSet.getInt("Wagon_w_id"));

        LOG.debug("JDBCSeatDao extractFromResultSet(): " + seat.toString());
        return seat;
    }

    @Override
    public List<Seat> findAll() {
        List<Seat> seatList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, TABLE_NAME))) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                seatList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOG.debug("JDBCSeatDao findAll() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        LOG.debug("JDBCSeatDao findAll()");
        return seatList;
    }

    @Override
    public void update(Seat seat) {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(QueryStringGetter.getQuery(QueryType.UPDATE, TABLE_NAME))) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            preparedStatement.setInt(1, seat.getWagonId());
            preparedStatement.setInt(2, seat.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            LOG.debug("JDBCSeatDao update()");
        } catch (SQLException e) {
            LOG.debug("JDBCSeatDao update() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Seat seat) {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(QueryStringGetter.getQuery(QueryType.DELETE, TABLE_NAME))) {
            preparedStatement.setInt(1, seat.getId());
            preparedStatement.execute();
            LOG.debug("JDBCSeatDao delete()");
        } catch (SQLException e) {
            LOG.debug("JDBCSeatDao delete() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        LOG.debug("JDBCSeatDao close()");
        connection.close();
    }

    public Connection getConnection() {
        LOG.debug("JDBCSeatDao getConnection()");
        return connection;
    }
}
