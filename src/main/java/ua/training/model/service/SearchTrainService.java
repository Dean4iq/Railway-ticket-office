package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.entity.Train;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SearchTrainService implements Service {
    private static final Logger log = LogManager.getLogger(SearchTrainService.class);

    @Override
    public String execute(HttpServletRequest request) {
        if (request.getParameter("trainNumberSubmit") != null) {

        } else if (request.getParameter("trainDestinationSubmit") != null) {

        } else if (request.getParameter("allTrainSubmit") != null) {
            DaoFactory daoFactory = JDBCDaoFactory.getInstance();

            try (TrainDao trainDao = daoFactory.createTrainDao()) {
                List<Train> trainList = trainDao.findAll();
                request.setAttribute("trainList", trainList);
                log.debug("Search all trains");
            } catch (Exception e) {
                log.debug("Exception while execute() allTrainSubmit");
                log.error(e.getStackTrace());
            }
        }
        log.debug("SearchTrainService execute()");
        return "/searchTrains.jsp";
    }
}
