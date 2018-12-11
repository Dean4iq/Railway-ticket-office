package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.entity.Train;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchTrainService implements Service {
    private static final Logger log = LogManager.getLogger(SearchTrainService.class);

    @Override
    public String execute(HttpServletRequest request) {
        if (request.getParameter("trainNumberSubmit") != null) {

        } else if (request.getParameter("trainDestinationSubmit") != null) {

        } else if (request.getParameter("allTrainSubmit") != null) {
            DaoFactory daoFactory = new JDBCDaoFactory();

            try (TrainDao trainDao = daoFactory.createTrainDao()) {
                List<Train> trainList = trainDao.findAll();
                request.setAttribute("trainList", trainList);
                log.debug("Search all trains");
            } catch (Exception e) {
                log.debug("Exception while execute() allTrainSubmit");
                log.error(e.getStackTrace());
            }
        }

        Calendar calendar = Calendar.getInstance();
        String minDate = getFormattedDate(calendar);

        calendar.setTimeInMillis(new Date().getTime() + 1_296_000_000);

        String maxDate = getFormattedDate(calendar);

        request.setAttribute("minDate", minDate);
        request.setAttribute("maxDate", maxDate);
        log.debug("SearchTrainService execute()");
        return "/searchTrains.jsp";
    }


    private String getFormattedDate(Calendar calendar) {
        return new StringBuilder()
                .append(calendar.get(Calendar.YEAR))
                .append('-')
                .append(calendar.get(Calendar.MONTH) + 1)
                .append('-')
                .append(calendar.get(Calendar.DATE)).toString();
    }
}
