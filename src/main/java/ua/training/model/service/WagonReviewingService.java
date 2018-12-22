package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.WagonDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.Train;
import ua.training.model.entity.Wagon;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class WagonReviewingService implements Service {
    private static final Logger LOG = LogManager.getLogger(WagonReviewingService.class);

    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("WagonReviewingService execute()");

        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            if (parameter.contains("wagonInTrain")) {
                int trainNumber = Integer.parseInt(parameter.substring("wagonInTrain".length()));
                System.out.println(trainNumber);
            }
        }
        return "/WEB-INF/user/wagons.jsp";
    }

    //TODO
    private List<Wagon> getWagons(int trainId){
        List<Train> trains = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try(TrainDao trainDao = daoFactory.createTrainDao()){

        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }
}
