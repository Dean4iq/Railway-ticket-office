package ua.training.model.dao.implement;

import ua.training.model.dao.RouteDao;
import ua.training.model.entity.Route;
import ua.training.model.entity.Station;
import ua.training.model.entity.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCRouteDao implements RouteDao {
    private Connection connection;

    public JDBCRouteDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Route route) {
        try (PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO route (tr_id, position, st_id, arrival, departure)" +
                        " VALUES (?, ?, ?, ?, ?)")) {
            connection.setAutoCommit(false);

            ps.setInt(1, route.getTrain().getId());
            ps.setInt(2, route.getPosition());
            ps.setInt(3, route.getStation().getId());
            ps.setTime(4, route.getArrivalTime());
            ps.setTime(5, route.getDepartureTime());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Route findById(int trainId) {
        Route route = new Route();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT * FROM route WHERE tr_id = ?")) {
            preparedStatement.setInt(1, trainId);

            route = extractFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return route;
    }

    static Route extractFromResultSet(ResultSet resultSet) throws SQLException {
        Route route = new Route();

        route.setPositionId(resultSet.getInt("position"));
        route.setArrivalTime(resultSet.getTime("arrival"));
        route.setDepartureTime(resultSet.getTime("departure"));
        route.setTrain(JDBCTrainDao.extractFromResultSet(resultSet));
        route.setStation(JDBCStationDao.extractFromResultSet(resultSet));

        return route;
    }

    @Override
    public List<Route> findAll() {
        List<Route> resultList = new ArrayList<>();
        Map<Integer, Train> trains = new HashMap<>();
        Map<Integer, Station> stations = new HashMap<>();

        try (Statement ps = connection.createStatement()) {
            ResultSet rs = ps.executeQuery("SELECT * FROM route r LEFT JOIN train t ON r.tr_id=t.tr_id LEFT JOIN station s ON r.st_id=s.st_id");

            while (rs.next()) {
                Route route = extractFromResultSet(rs);

                Train train = makeUniqueTrain(trains, route.getTrain());
                Station station = makeUniqueStation(stations, route.getStation());

                route.setTrain(train);
                route.setStation(station);

                resultList.add(route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private Train makeUniqueTrain(Map<Integer, Train> trains, Train train) {
        trains.putIfAbsent(train.getId(), train);
        return trains.get(train.getId());
    }

    private Station makeUniqueStation(Map<Integer, Station> stations, Station station) {
        stations.putIfAbsent(station.getId(), station);
        return stations.get(station.getId());
    }

    @Override
    public void update(Route route) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE route SET st_id=?, arrival=?, departure=? " +
                        "WHERE tr_id = ? AND position=?")) {
            preparedStatement.setInt(1, route.getStation().getId());
            preparedStatement.setTime(2, route.getArrivalTime());
            preparedStatement.setTime(3, route.getDepartureTime());
            preparedStatement.setInt(4, route.getTrain().getId());
            preparedStatement.setInt(5, route.getPosition());

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
