package ua.training.model.dao.implement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.RouteDao;
import ua.training.model.entity.Route;
import ua.training.model.entity.Station;
import ua.training.model.entity.Train;

import java.sql.*;
import java.util.*;

public class JDBCRouteDao implements RouteDao {
    private static final Logger log = LogManager.getLogger(JDBCRouteDao.class);

    private Connection connection;

    public JDBCRouteDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCRouteDao constructor()");
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

            connection.commit();
            log.debug("JDBCRouteDao create()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao create() failed");
            log.error(Arrays.toString(e.getStackTrace()));
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
            log.debug("JDBCRouteDao findById()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao findById() failed");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return route;
    }

    static Route extractFromResultSet(ResultSet resultSet) throws SQLException {
        Route route = new Route();

        route.setTrainId(resultSet.getInt("Train_t_id"));
        route.setStationId(resultSet.getInt("Station_st_id"));
        route.setArrivalTime(resultSet.getTime("arrival"));
        route.setDepartureTime(resultSet.getTime("departure"));

        log.debug("JDBCRouteDao extractFromResultSet()");

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

            log.debug("JDBCRouteDao findAll()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao findAll() failed");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return resultList;
    }

    private Train makeUniqueTrain(Map<Integer, Train> trains, Train train) {
        trains.putIfAbsent(train.getId(), train);

        log.debug("JDBCRouteDao makeUniqueTrain()");
        return trains.get(train.getId());
    }

    private Station makeUniqueStation(Map<Integer, Station> stations, Station station) {
        stations.putIfAbsent(station.getId(), station);

        log.debug("JDBCRouteDao makeUniqueStation()");
        return stations.get(station.getId());
    }

    @Override
    public void update(Route route) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE route SET st_id=?, arrival=?, departure=? " +
                        "WHERE tr_id = ? AND position=?")) {

            preparedStatement.executeUpdate();
            log.debug("JDBCRouteDao update()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao update() failed");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("DELETE FROM station WHERE st_id = ?")) {
            preparedStatement.setInt(1, id);

            preparedStatement.execute();
            log.debug("JDBCRouteDao delete()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao delete() failed");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("JDBCRouteDao close()");
        connection.close();
    }
}
