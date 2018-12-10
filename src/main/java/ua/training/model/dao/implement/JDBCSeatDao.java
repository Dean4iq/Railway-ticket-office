package ua.training.model.dao.implement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.SeatDao;
import ua.training.model.entity.Seat;
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

public class JDBCSeatDao implements SeatDao {
    private static final Logger log = LogManager.getLogger(JDBCRouteDao.class);
    private static final TableName tableName = TableName.SEAT;

    private Connection connection;

    public JDBCSeatDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCSeatDao constructor()");
    }

    @Override
    public void create(Seat seat) {
        try (PreparedStatement ps = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            ps.setInt(1, seat.getWagonId());
            ps.setBoolean(2, seat.isOccupied());
            ps.execute();

            log.debug("JDBCSeatDao create()");
        } catch (SQLException e) {
            log.debug("JDBCSeatDao create() failed: " + seat.toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    public boolean bookSeat(Seat seat) {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            preparedStatement.setInt(1, seat.getWagonId());
            preparedStatement.setBoolean(2, seat.isOccupied());
            preparedStatement.setInt(3, seat.getId());
            preparedStatement.executeUpdate();

            Thread.sleep(1000000);

            //TODO
            if (true) {
                log.debug("JDBCSeatDao bookSeat() commit");
                connection.commit();
            } else {
                log.debug("JDBCSeatDao bookSeat() rollback");
                connection.rollback();
            }
        } catch (SQLException | InterruptedException e) {
            log.debug("JDBCSeatDao bookSeat() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return false;
    }

    @Override
    public Seat findById(Integer id) {
        Seat seat = new Seat();

        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                seat = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.debug("JDBCSeatDao findById() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCSeatDao findById()");
        return seat;
    }

    static Seat extractFromResultSet(ResultSet resultSet) throws SQLException {
        Seat seat = new Seat();

        seat.setId(resultSet.getInt("s_id"));
        seat.setWagonId(resultSet.getInt("Wagon_w_id"));
        seat.setOccupied(resultSet.getBoolean("occupied"));

        log.debug("JDBCSeatDao extractFromResultSet(): " + seat.toString());
        return seat;
    }

    @Override
    public List<Seat> findAll() {
        List<Seat> seatList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName))) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                seatList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.debug("JDBCSeatDao findAll() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCSeatDao findAll()");
        return seatList;
    }

    @Override
    public void update(Seat seat) {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            preparedStatement.setInt(1, seat.getWagonId());
            preparedStatement.setBoolean(2, seat.isOccupied());
            preparedStatement.setInt(3, seat.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            log.debug("JDBCSeatDao update()");
        } catch (SQLException e) {
            log.debug("JDBCSeatDao update() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Seat seat) {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setInt(1, seat.getId());
            preparedStatement.execute();
            log.debug("JDBCSeatDao delete()");
        } catch (SQLException e) {
            log.debug("JDBCSeatDao delete() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("JDBCSeatDao close()");
        connection.close();
    }

    public Connection getConnection() {
        log.debug("JDBCSeatDao getConnection()");
        return connection;
    }
}
