package ua.training.model.dao.daoimplementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.TicketDao;
import ua.training.model.entity.Ticket;
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

public class JDBCTicketDao implements TicketDao {
    private static final Logger log = LogManager.getLogger(JDBCTicketDao.class);
    private static final TableName tableName = TableName.TICKET;

    private Connection connection;

    public JDBCTicketDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCTicketDao constructor()");
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
            log.debug("JDBCTicketDao create()");
        } catch (SQLException e) {
            log.debug("JDBCTicketDao create() failed: " + ticket.toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
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
            log.debug("JDBCTicketDao findById() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCTicketDao findById()");
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

        log.debug("JDBCTicketDao extractFromResultSet(): " + ticket.toString());
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
            log.debug("JDBCTicketDao findAll() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCTicketDao findAll()");
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
            log.debug("JDBCTicketDao update()");
        } catch (SQLException e) {
            log.debug("JDBCTicketDao update() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Ticket ticket) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setInt(1, ticket.getId());

            preparedStatement.execute();
            log.debug("JDBCTicketDao delete()");
        } catch (SQLException e) {
            log.debug("JDBCTicketDao delete() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("JDBCTicketDao close()");
        connection.close();
    }
}
