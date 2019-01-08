package ua.training.model.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.TicketDao;
import ua.training.model.entity.*;
import ua.training.model.util.QueryStringGetter;
import ua.training.model.util.QueryType;
import ua.training.model.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCTicketDao implements TicketDao {
    private static final Logger LOG = LogManager.getLogger(JDBCTicketDao.class);
    private static final TableName tableName = TableName.TICKET;

    private boolean permissionToClose = true;

    private Connection connection;

    public JDBCTicketDao(Connection connection) {
        this.connection = connection;
        LOG.debug("JDBCTicketDao constructor()");
    }

    public Connection createWithoutCommit(Ticket ticket){
        LOG.debug("Creating ticket without commit");
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            preparedStatement.setString(1, ticket.getUserLogin());
            preparedStatement.setInt(2, ticket.getSeatId());
            preparedStatement.setInt(3, ticket.getCost());
            preparedStatement.setDate(4, ticket.getTravelDate());
            preparedStatement.setInt(5, ticket.getDepartureStationId());
            preparedStatement.setInt(6, ticket.getArrivalStationId());
            preparedStatement.setInt(7, ticket.getTrainId());

            preparedStatement.executeUpdate();

            permissionToClose = false;

            return getConnection();
        } catch (SQLException e) {
            LOG.debug("JDBCTicketDao create() failed: " + ticket.toString());
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    @Override
    public void create(Ticket ticket) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            preparedStatement.setString(1, ticket.getUserLogin());
            preparedStatement.setInt(2, ticket.getSeatId());
            preparedStatement.setInt(3, ticket.getCost());
            preparedStatement.setDate(4, ticket.getTravelDate());
            preparedStatement.setInt(5, ticket.getDepartureStationId());
            preparedStatement.setInt(6, ticket.getArrivalStationId());
            preparedStatement.setInt(7, ticket.getTrainId());

            preparedStatement.execute();
            connection.commit();
            LOG.debug("JDBCTicketDao create()");
        } catch (SQLException e) {
            LOG.debug("JDBCTicketDao create() failed: " + ticket.toString());
            LOG.error(Arrays.toString(e.getStackTrace()));
            connection.rollback();
        }
    }

    public List<Ticket> findByTrainId(Integer trainId) {
        List<Ticket> ticketList = new ArrayList<>();
        Map<Integer, Seat> seatMap = new HashMap<>();
        Map<Integer, Wagon> wagonMap = new HashMap<>();
        Map<Integer, Train> trainMap = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND_BY_TRAIN, tableName))) {
            preparedStatement.setInt(1, trainId);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = extractFromResultSet(resultSet);
                Seat seat = JDBCSeatDao.extractFromResultSet(resultSet);
                Wagon wagon = JDBCWagonDao.extractFromResultSet(resultSet);
                Train train = JDBCTrainDao.extractFromResultSet(resultSet);

                wagon = makeUniqueWagon(wagonMap, wagon);

                ticket.setSeat(makeUniqueSeat(seatMap, seat));
                ticket.setWagonId(wagon.getId());
                ticket.setTrain(makeUniqueTrain(trainMap, train));

                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
        return ticketList;
    }

    @Override
    public Ticket findById(Integer id) {
        Ticket ticket = new Ticket();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            preparedStatement.setInt(1, id);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ticket = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOG.debug("JDBCTicketDao findById() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        LOG.debug("JDBCTicketDao findById()");
        return ticket;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> ticketList = new ArrayList<>();

        Map<Integer, Seat> seatMap = new HashMap<>();
        Map<Integer, Wagon> wagonMap = new HashMap<>();
        Map<Integer, Train> trainMap = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName))) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = extractFromResultSet(resultSet);
                Seat seat = JDBCSeatDao.extractFromResultSet(resultSet);
                Wagon wagon = JDBCWagonDao.extractFromResultSet(resultSet);
                Train train = JDBCTrainDao.extractFromResultSet(resultSet);

                wagon = makeUniqueWagon(wagonMap, wagon);

                ticket.setSeat(makeUniqueSeat(seatMap, seat));
                ticket.setWagonId(wagon.getId());
                ticket.setTrain(makeUniqueTrain(trainMap, train));

                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        LOG.debug("JDBCTicketDao findAll()");
        return ticketList;
    }

    @Override
    public void update(Ticket ticket) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            preparedStatement.setString(1, ticket.getUserLogin());
            preparedStatement.setInt(2, ticket.getSeatId());
            preparedStatement.setInt(3, ticket.getCost());
            preparedStatement.setDate(4, ticket.getTravelDate());
            preparedStatement.setInt(5, ticket.getDepartureStationId());
            preparedStatement.setInt(6, ticket.getArrivalStationId());
            preparedStatement.setInt(7, ticket.getTrainId());
            preparedStatement.setInt(8, ticket.getId());

            preparedStatement.executeUpdate();
            LOG.debug("JDBCTicketDao update()");
        } catch (SQLException e) {
            LOG.debug("JDBCTicketDao update() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Ticket ticket) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setInt(1, ticket.getId());
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            preparedStatement.execute();
            LOG.debug("JDBCTicketDao delete()");
        } catch (SQLException e) {
            LOG.debug("JDBCTicketDao delete() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        LOG.debug("JDBCTicketDao close()");

        if (permissionToClose) {
            connection.close();
        }
    }

    static Ticket extractFromResultSet(ResultSet resultSet) throws SQLException {
        Ticket ticket = new Ticket();

        ticket.setId(resultSet.getInt("tick_id"));
        ticket.setUserLogin(resultSet.getString("User_login"));
        ticket.setSeatId(resultSet.getInt("Seat_s_id"));
        ticket.setCost(resultSet.getInt("cost"));
        ticket.setTravelDate(resultSet.getDate("date"));
        ticket.setDepartureStationId(resultSet.getInt("departure_st_id"));
        ticket.setArrivalStationId(resultSet.getInt("arrival_st_id"));
        ticket.setTrainId(resultSet.getInt("Train_t_id"));

        LOG.debug("JDBCTicketDao extractFromResultSet(): " + ticket.toString());
        return ticket;
    }

    private Connection getConnection() {
        return connection;
    }

    private Seat makeUniqueSeat(Map<Integer, Seat> seatMap, Seat seat) {
        seatMap.putIfAbsent(seat.getId(), seat);

        return seatMap.get(seat.getId());
    }

    private Wagon makeUniqueWagon(Map<Integer, Wagon> wagonMap, Wagon wagon) {
        wagonMap.putIfAbsent(wagon.getId(), wagon);

        return wagonMap.get(wagon.getId());
    }

    private Train makeUniqueTrain(Map<Integer, Train> trainMap, Train train) {
        trainMap.putIfAbsent(train.getId(), train);

        return trainMap.get(train.getId());
    }
}
