package ua.training.model.dao.implementation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ua.training.model.dao.TicketDao;
import ua.training.model.entity.Ticket;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class JDBCTicketDaoTest {
    @Mock
    Connection connection = Mockito.mock(Connection.class);
    @Mock
    ResultSet resultSet = Mockito.mock(ResultSet.class);
    @Mock
    PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

    private TicketDao ticketDao;

    private Ticket ticket;

    @Before
    public void setUp() throws Exception {
        ticket = new Ticket();

        ticket.setId(33);
        ticket.setSeatId(13);
        ticket.setTrainId(42);
        ticket.setUserLogin("PaulHogan");
        ticket.setCost(450);
        ticket.setTravelDate(new Date(new java.util.Date().getTime()));
        ticket.setDepartureStationId(12);
        ticket.setArrivalStationId(14);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("tick_id")).thenReturn(ticket.getId());
        when(resultSet.getString("User_login")).thenReturn(ticket.getUserLogin());
        when(resultSet.getInt("Seat_s_id")).thenReturn(ticket.getSeatId());
        when(resultSet.getInt("cost")).thenReturn(ticket.getCost());
        when(resultSet.getDate("date")).thenReturn(ticket.getTravelDate());
        when(resultSet.getInt("departure_st_id")).thenReturn(ticket.getDepartureStationId());
        when(resultSet.getInt("arrival_st_id")).thenReturn(ticket.getArrivalStationId());
        when(resultSet.getInt("Train_t_id")).thenReturn(ticket.getTrainId());

        ticketDao = new JDBCTicketDao(connection);
    }

    @Test
    public void createWithoutCommit() {
        Connection connection = ticketDao.createWithoutCommit(ticket);

        assertEquals(this.connection, connection);
    }

    @Test
    public void findByTrainId() {
        List<Ticket> testTicketList = ticketDao.findByTrainId(42);

        assertEquals(ticket, testTicketList.get(0));
    }

    @Test
    public void findById() {
        Ticket testTicket = ticketDao.findById(33);

        assertEquals(ticket, testTicket);
    }

    @Test
    public void findAll() {
        List<Ticket> testTicketList = ticketDao.findAll();

        assertEquals(ticket, testTicketList.get(0));
    }
}