package ua.training.model.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.UserDao;
import ua.training.model.entity.User;
import ua.training.util.QueryStringGetter;
import ua.training.util.QueryType;
import ua.training.util.TableName;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDBCUserDao implements UserDao {
    private static final Logger log = LogManager.getLogger(JDBCUserDao.class);
    private static final TableName tableName = TableName.USER;

    private Connection connection;

    public JDBCUserDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCUserDao constructor()");
    }

    @Override
    public void create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Argument 'user' is null");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getNameUA());
            preparedStatement.setString(6, user.getLastNameUA());
            preparedStatement.setBoolean(7, user.isAdmin());

            preparedStatement.execute();
            log.debug("JDBCUserDao create()");
        } catch (SQLException e) {
            log.debug("JDBCUserDao create() failed: " + user.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public User findById(String login) {
        User user = new User();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            log.debug("JDBCUserDao findById() error");
        }

        log.debug("JDBCUserDao findById()");
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

        log.debug("JDBCUserDao extractFromResultSet(): " + user.toString());
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                userList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.debug("JDBCUserDao findAll() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCUserDao findAll()");
        return userList;
    }

    @Override
    public void update(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getNameUA());
            preparedStatement.setString(5, user.getLastNameUA());
            preparedStatement.setBoolean(6, user.isAdmin());
            preparedStatement.setString(7, user.getLogin());

            preparedStatement.executeUpdate();
            log.debug("JDBCUserDao update()");
        } catch (SQLException e) {
            log.debug("JDBCUserDao update() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setString(1, user.getLogin());

            preparedStatement.execute();
            log.debug("JDBCUserDao delete()");
        } catch (SQLException e) {
            log.debug("JDBCUserDao delete() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("JDBCUserDao close()");
        connection.close();
    }
}
