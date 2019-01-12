package ua.training.model.dao.implementation;

import ua.training.model.dao.*;

import java.sql.Connection;

public class JDBCDaoFactory extends DaoFactory {
    @Override
    public RouteDao createRouteDao() {
        return new JDBCRouteDao(getConnection());
    }

    @Override
    public SeatDao createSeatDao() {
        return new JDBCSeatDao(getConnection());
    }

    @Override
    public StationDao createStationDao() {
        return new JDBCStationDao(getConnection());
    }

    @Override
    public TicketDao createTicketDao() {
        return new JDBCTicketDao(getConnection());
    }

    @Override
    public TrainDao createTrainDao() {
        return new JDBCTrainDao(getConnection());
    }

    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(getConnection());
    }

    @Override
    public WagonDao createWagonDao() {
        return new JDBCWagonDao(getConnection());
    }

    private Connection getConnection() {
        return ConnectorDB.INSTANCE.getConnection();
    }
}
