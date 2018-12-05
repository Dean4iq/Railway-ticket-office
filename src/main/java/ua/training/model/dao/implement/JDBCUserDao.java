package ua.training.model.dao.implement;

import ua.training.model.dao.UserDao;
import ua.training.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUserDao implements UserDao {
    private Connection connection;

    public JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User user) {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement("INSERT INTO User VALUES (?,?,?,?,?,?,?)")) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getNameUA());
            preparedStatement.setString(6, user.getLastNameUA());
            preparedStatement.setBoolean(7, user.isAdmin());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(String id) {
        User user = new User();

        try (PreparedStatement preparedStatement
                     = connection.prepareStatement("SELECT * FROM User WHERE login=?")) {
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    static User extractFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setNameUA(resultSet.getString("name_ua"));
        user.setLastNameUA(resultSet.getString("last_name_ua"));
        user.setAdmin(resultSet.getBoolean("admin"));

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM User");

            while (resultSet.next()) {
                userList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
