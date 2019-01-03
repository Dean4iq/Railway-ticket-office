package ua.training.model.dao.implementation;

import ua.training.model.dao.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    private Connection getConnection(){
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/finalproject",
                    "user" ,
                    "pass" );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}