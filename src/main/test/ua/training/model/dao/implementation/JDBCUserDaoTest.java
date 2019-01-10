package ua.training.model.dao.implementation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ua.training.model.dao.UserDao;
import ua.training.model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class JDBCUserDaoTest {
    @Mock
    Connection connection = Mockito.mock(Connection.class);
    @Mock
    ResultSet resultSet = Mockito.mock(ResultSet.class);
    @Mock
    PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

    private UserDao userDao;

    private User user1;
    private User user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user2 = null;

        user1.setLogin("WillWillSmithSmith");
        user1.setPassword("WillSmith");
        user1.setName("Will");
        user1.setLastName("Smith");
        user1.setNameUA("Уілл");
        user1.setLastNameUA("Коваль");
        user1.setAdmin(false);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("login")).thenReturn(user1.getLogin());
        when(resultSet.getString("password")).thenReturn(user1.getPassword());
        when(resultSet.getString("name")).thenReturn(user1.getName());
        when(resultSet.getString("last_name")).thenReturn(user1.getLastName());
        when(resultSet.getString("name_ua")).thenReturn(user1.getNameUA());
        when(resultSet.getString("last_name_ua")).thenReturn(user1.getLastNameUA());
        when(resultSet.getBoolean("admin")).thenReturn(user1.isAdmin());

        userDao = new JDBCUserDao(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithIllegalArgument() throws SQLException {
        userDao.create(user2);
    }

    @Test
    public void findById() {
        User user = userDao.findById("CrocodileMan");

        assertEquals(user1, user);
    }

    @Test
    public void findAllNotEmpty() {
        List<User> userList = userDao.findAll();

        assertTrue(userList.size() > 0);
    }

    @Test
    public void findAllExactlySize() {
        List<User> userList = userDao.findAll();

        assertEquals(2, userList.size());
    }

    @Test
    public void findAll() {
        List<User> userList = userDao.findAll();

        assertEquals(userList.get(0), user1);
    }
}