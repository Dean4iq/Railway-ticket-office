package ua.training.model.dao.daoimplementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.TicketDao;
import ua.training.model.entity.*;
import ua.training.util.QueryStringGetter;
import ua.training.util.QueryType;
import ua.training.util.TableName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCTicketDao implements TicketDao {
    private static final Logger LOG = LogManager.getLogger(JDBCTicketDao.class);
    private static final TableName tableName = TableName.TICKET;

    private Connection connection;

    public JDBCTicketDao(Connection connection) {
        this.connection = connection;
        LOG.debug("JDBCTicketDao constructor()");
    }

    @Override
    public void create(Ticket ticket) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            preparedStatement.setString(1, ticket.getUserLogin());
            preparedStatement.setInt(2, ticket.getSeatId());
            preparedStatement.setInt(3, ticket.getCost());
            preparedStatement.setDate(4, ticket.getTravelDate());
            preparedStatement.setInt(5, ticket.getDepartureStationId());
            preparedStatement.setInt(6, ticket.getArrivalStationId());
            preparedStatement.setInt(7, ticket.getTrainId());

            preparedStatement.execute();
            LOG.debug("JDBCTicketDao create()");
        } catch (SQLException e) {
            LOG.debug("JDBCTicketDao create() failed: " + ticket.toString());
            LOG.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
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

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = extractFromResultSet(resultSet);
                Seat seat = JDBCSeatDao.extractFromResultSet(resultSet);
                Wagon wagon = JDBCWagonDao.extractFromResultSet(resultSet);
                Train train = JDBCTrainDao.extractFromResultSet(resultSet);

                ticket.setSeat(makeUniqueSeat(seatMap, seat));
                ticket.setWagon(makeUniqueWagon(wagonMap, wagon));
                ticket.setTrain(makeUniqueTrain(trainMap, train));

                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
        return ticketList;
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

    private Station makeUniqueStation(Map<Integer, Station> stationMap, Station station) {
        stationMap.putIfAbsent(station.getId(), station);

        return stationMap.get(station.getId());
    }

    @Override
    public Ticket findById(Integer id) {
        Ticket ticket = new Ticket();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            preparedStatement.setInt(1, id);

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

    @Override
    public List<Ticket> findAll() {
        List<Ticket> ticketList = new ArrayList<>();

        //TODO
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ticketList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOG.debug("JDBCTicketDao findAll() error");
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        LOG.debug("JDBCTicketDao findAll()");
        return ticketList;
    }

    @Override
    public void update(Ticket ticket) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
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
        connection.close();
    }
}
