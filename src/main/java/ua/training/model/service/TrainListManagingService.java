package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.Train;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TrainListManagingService implements Service {
    private static final Logger log = LogManager.getLogger(TrainListManagingService.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("TrainListManagingService execute()");

        try {
            DaoFactory daoFactory = JDBCDaoFactory.getInstance();
            TrainDao trainDao = daoFactory.createTrainDao();

            List<Train> trains = trainDao.findAll();

            request.setAttribute("trainList", trains);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/WEB-INF/admin/trainList.jsp";
    }
}
