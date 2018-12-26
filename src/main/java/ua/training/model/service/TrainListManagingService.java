package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.Train;

import java.util.ArrayList;
import java.util.List;

public class TrainListManagingService {
    private static final Logger LOG = LogManager.getLogger(TrainListManagingService.class);

    public static List<Train> getTrainList() {
        LOG.debug("getTrains()");
        List<Train> trains = new ArrayList<>();

        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        try (TrainDao trainDao = daoFactory.createTrainDao()){
            trains = trainDao.findAll();
        } catch (Exception e) {
            LOG.error("Error: {}", e);
        }

        return trains;
    }

    public static List<Train> getAllTrainList() {
        LOG.debug("getAllTrains()");
        List<Train> trains = new ArrayList<>();

        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        try (TrainDao trainDao = daoFactory.createTrainDao()){
            trains = trainDao.getEveryTrain();
        } catch (Exception e) {
            LOG.error("Error: {}", e);
        }

        return trains;
    }

    public static void deleteTrain(int trainId){
        LOG.debug("deleteTrain()");

        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        try(TrainDao trainDao = daoFactory.createTrainDao()){
            Train train = new Train();
            train.setId(trainId);

            trainDao.delete(train);
        } catch (Exception e) {
            LOG.error("Error: {}", e);
        }
    }
}
