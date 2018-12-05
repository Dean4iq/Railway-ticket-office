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
                ("INSERT INTO route (Train_t_id, Station_st_id, arrival, departure)" +
                        " VALUES (?, ?, ?, ?)")) {
            connection.setAutoCommit(false);

            ps.setInt(1, route.getTrainId());
            ps.setInt(2, route.getStationId());
            ps.setTime(3, route.getArrivalTime());
            ps.setTime(4, route.getDepartureTime());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Route findById(Integer trainId) {
        Route route = new Route();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT * FROM Route WHERE Train_t_id = ?")) {
            preparedStatement.setInt(1, trainId);

            route = extractFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return route;
    }

    static Route extractFromResultSet(ResultSet resultSet) throws SQLException {
        Route route = new Route();

        route.setTrainId(resultSet.getInt("Train_t_id"));
        route.setStationId(resultSet.getInt("Station_st_id"));
        route.setArrivalTime(resultSet.getTime("arrival"));
        route.setDepartureTime(resultSet.getTime("departure"));

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

                /*Train train = makeUniqueTrain(trains, route.getTrain());
                Station station = makeUniqueStation(stations, route.getStation());

                route.setTrain(train);
                route.setStation(station);*/

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

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
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
