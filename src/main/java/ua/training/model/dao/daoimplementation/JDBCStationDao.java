package ua.training.model.dao.daoimplementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.StationDao;
import ua.training.model.entity.Station;
import ua.training.util.QueryStringGetter;
import ua.training.util.QueryType;
import ua.training.util.TableName;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDBCStationDao implements StationDao {
    private static final Logger log = LogManager.getLogger(JDBCStationDao.class);
    private static TableName tableName = TableName.STATION;

    private Connection connection;

    public JDBCStationDao(Connection connection) {
        this.connection = connection;
        log.debug("JDBCStationDao constructor()");
    }

    @Override
    public void create(Station station) {
        try (PreparedStatement ps = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.INSERT, tableName))) {
            ps.setString(1, station.getName());
            ps.execute();

            log.debug("JDBCStationDao create()");
        } catch (SQLException e) {
            log.debug("JDBCStationDao create() failed: " + station.toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Station findById(Integer id) {
        Station station = new Station();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.FIND, tableName))) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                station = extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.debug("JDBCStationDao findById() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCStationDao findById()");
        return station;
    }

    static Station extractFromResultSet(ResultSet resultSet) throws SQLException {
        Station station = new Station();

        station.setId(resultSet.getInt("st_id"));
        station.setName(resultSet.getString("name"));
        station.setNameUA(resultSet.getString("name_UA"));

        log.debug("JDBCStationDao extractFromResultSet(): " + station.toString());
        return station;
    }

    @Override
    public List<Station> findAll() {
        List<Station> stationList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.SELECT, tableName));
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                stationList.add(extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.debug("JDBCStationDao findAll() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("JDBCStationDao findAll()");
        return stationList;
    }

    @Override
    public void update(Station station) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.UPDATE, tableName))) {
            preparedStatement.setString(1, station.getName());
            preparedStatement.setInt(2, station.getId());

            preparedStatement.executeUpdate();
            log.debug("JDBCStationDao update()");
        } catch (SQLException e) {
            log.debug("JDBCStationDao update() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void delete(Station station) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (QueryStringGetter.getQuery(QueryType.DELETE, tableName))) {
            preparedStatement.setInt(1, station.getId());

            preparedStatement.execute();
            log.debug("JDBCStationDao delete()");
        } catch (SQLException e) {
            log.debug("JDBCStationDao delete() error");
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("JDBCStationDao close()");
        connection.close();
    }
}
