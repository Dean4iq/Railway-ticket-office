package ua.training.model.dao.implement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.RouteDao;
import ua.training.model.entity.Route;
import ua.training.model.entity.Station;
import ua.training.model.entity.Train;
import ua.training.util.QueryStringGetter;
import ua.training.util.QueryType;
import ua.training.util.TableName;

import java.sql.*;
import java.util.*;

public class JDBCRouteDao implements RouteDao {
    private static final Logger log = LogManager.getLogger(JDBCRouteDao.class);
    private static final TableName tableName = TableName.ROUTE;

    private Connection connection;

    public JDBCRouteDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCRouteDao constructor()");
    }

    @Override
    public void create(Route route) {
        try (PreparedStatement ps = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {

            ps.setInt(1, route.getTrainId());
            ps.setInt(2, route.getStationId());
            ps.setTime(3, route.getArrivalTime());
            ps.setTime(4, route.getDepartureTime());
            ps.execute();

            log.debug("JDBCRouteDao create()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao create() failed: " + route.toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Route findById(Integer trainId) {
        Route route = new Route();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            preparedStatement.setInt(1, trainId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                route = extractFromResultSet(resultSet);
            }

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

        log.debug("JDBCRouteDao extractFromResultSet(): " + route.toString());

        return route;
    }

    @Override
    public List<Route> findAll() {
        List<Route> routeList = new ArrayList<>();
        //Map<Integer, Train> trains = new HashMap<>();
        //Map<Integer, Station> stations = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Route route = extractFromResultSet(resultSet);

                /*Train train = makeUniqueTrain(trains, route.getTrain());
                Station station = makeUniqueStation(stations, route.getStation());

                route.setTrain(train);
                route.setStation(station);*/

                routeList.add(route);
            }

            log.debug("JDBCRouteDao findAll()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao findAll() failed");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return routeList;
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
                (QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {

            preparedStatement.setTime(1, route.getArrivalTime());
            preparedStatement.setTime(2, route.getDepartureTime());
            preparedStatement.setInt(3, route.getTrainId());
            preparedStatement.setInt(4, route.getStationId());

            preparedStatement.executeUpdate();
            log.debug("JDBCRouteDao update()");
        } catch (SQLException e) {
            log.debug("JDBCRouteDao update() failed");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Route route) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setInt(1, route.getTrainId());
            preparedStatement.setInt(2, route.getStationId());

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
