package ua.training.model.dao.implement;

import ua.training.model.dao.StationDao;
import ua.training.model.entity.Station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCStationDao implements StationDao {
    private Connection connection;

    public JDBCStationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Station station) {
        try (PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO station (st_id, name)" +
                        " VALUES (?, ?)")) {
            connection.setAutoCommit(false);

            ps.setInt(1, station.getId());
            ps.setString(2, station.getName());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Station findById(int id) {
        Station station = new Station();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT * FROM station WHERE st_id = ?")) {
            preparedStatement.setInt(1, id);

            station = extractFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return station;
    }

    static Station extractFromResultSet(ResultSet resultSet) throws SQLException {
        Station station = new Station();

        station.setId(resultSet.getInt("st_id"));
        station.setName(resultSet.getString("name"));

        return station;
    }

    @Override
    public List<Station> findAll() {
        List<Station> resultList = new ArrayList<>();

        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM station");

            while (resultSet.next()){
                resultList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public void update(Station station) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE station SET name=? WHERE st_id = ?")) {
            preparedStatement.setString(1, station.getName());
            preparedStatement.setInt(2, station.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("DELETE FROM station WHERE st_id = ?")) {
            preparedStatement.setInt(1, id);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
