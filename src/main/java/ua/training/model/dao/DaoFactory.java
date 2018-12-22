package ua.training.model.dao;

import ua.training.model.dao.daoimplementation.JDBCDaoFactory;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract RouteDao createRouteDao();

    public abstract SeatDao createSeatDao();

    public abstract StationDao createStationDao();

    public abstract TicketDao createTicketDao();

    public abstract TrainDao createTrainDao();

    public abstract UserDao createUserDao();

    public abstract WagonDao createWagonDao();

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    DaoFactory temp = new JDBCDaoFactory();
                    daoFactory = temp;
                }
            }
        }
        return daoFactory;
    }
}
