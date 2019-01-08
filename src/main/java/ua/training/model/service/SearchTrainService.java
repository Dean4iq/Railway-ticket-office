package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.StationDao;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.implementation.JDBCDaoFactory;
import ua.training.model.entity.Station;
import ua.training.model.entity.Train;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class SearchTrainService {
    private static final Logger LOG = LogManager.getLogger(SearchTrainService.class);
    private static AttributeResourceManager attrManag = AttributeResourceManager.INSTANCE;

    public static List<Train> findTrainById(int trainNumber) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        List<Train> trainList = new ArrayList<>();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            trainList.add(trainDao.findById(trainNumber));

            LOG.debug("Search train by number");
        } catch (Exception e) {
            LOG.debug("Exception while execute() Find by id");
            LOG.error("Exception while execute() Find by id: {}", e);
        }

        return trainList;
    }

    public static List<Train> findAllTrains() {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        List<Train> trains = new ArrayList<>();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            trains = trainDao.findAll();

            LOG.debug("Search all trains");
        } catch (Exception e) {
            LOG.debug("Exception while execute() allTrainSubmit getAllTrain");
            LOG.error("Exception while execute() allTrainSubmit getAllTrain: {}",
                    Arrays.toString(e.getStackTrace()));
        }

        return trains;
    }

    public static void setStationList(HttpServletRequest request) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try(StationDao stationDao = daoFactory.createStationDao()){
            List<Station> stationList = stationDao.findAll();
            request.setAttribute(attrManag.getString(AttributeSources.STATION_LIST), stationList);
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
