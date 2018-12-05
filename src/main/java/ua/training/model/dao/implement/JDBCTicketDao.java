package ua.training.model.dao.implement;

import ua.training.model.dao.TicketDao;
import ua.training.model.entity.Seat;
import ua.training.model.entity.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCTicketDao implements TicketDao {
    private Connection connection;

    public JDBCTicketDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Ticket entity) {

    }

    @Override
    public Ticket findById(Integer id) {
        Ticket ticket = new Ticket();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM Ticket WHERE tick_id=?")) {
            preparedStatement.setInt(1, id);

            ticket = extractFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticket;
    }

    static Ticket extractFromResultSet(ResultSet resultSet) throws SQLException {
        Ticket ticket = new Ticket();

        ticket.setId(resultSet.getInt("tick_id"));
        ticket.setUserId(resultSet.getInt("User_id"));
        ticket.setSeatId(resultSet.getInt("TripInfo_trip_id"));
        ticket.setTripInfoId(resultSet.getInt("Seat_s_id"));
        ticket.setCost(resultSet.getInt("cost"));
        ticket.setTravelDate(resultSet.getTime("date"));
        ticket.setDepartureStationId(resultSet.getInt("departure_st_id"));
        ticket.setArrivalStationId(resultSet.getInt("arrival_st_id"));

        return ticket;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> ticketList = new ArrayList<>();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM Ticket" +
                             "INNER JOIN Seat ON Seat_s_id=s_id" +
                             "INNER JOIN Wagon ON Wagon_w_id=w_id" +
                             "INNER JOIN TripInfo ON TripInfo_trip_id=trip_id" +
                             "INNER JOIN Station ON departure_st_id=st_id AND arrive_st_id=st_id")) {
            connection.setAutoCommit(false);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(resultSet.getInt("tick_id"));
                ticket.setUserId(resultSet.getInt("User_id"));
                ticket.setTripInfoId(resultSet.getInt("TripInfo_trip_id"));
                ticket.setSeatId(resultSet.getInt("Seat_s_id"));
                ticket.setCost(resultSet.getInt("cost"));
                ticket.setTravelDate(resultSet.getTime("date"));
                ticket.setDepartureStationId(resultSet.getInt("departure_st_id"));
                ticket.setUserId(resultSet.getInt("arrival_st_id"));

                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketList;
    }

    @Override
    public void update(Ticket entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
