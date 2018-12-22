package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TrainDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.Train;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

public class SearchTicketService implements Service {
    private static final Logger log = LogManager.getLogger(SearchTrainService.class);

    @Override
    public String execute(HttpServletRequest request) {
        if (request.getParameter("SwitchDirections") != null) {
        } else if (request.getParameter("ticketSearchSubmit") != null) {
            List<Train> trainList = getTrainList();
            setTravelDate(request, trainList);
            request.setAttribute("trainList", trainList);
        } else if (request.getParameterNames().hasMoreElements() &&
                request.getParameterNames().nextElement().contains("wagonInTrain")) {
            return new WagonReviewingService().execute(request);
        }

        Calendar calendar = Calendar.getInstance();
        String minCalendarDate = getFormattedDate(calendar);

        calendar.setTimeInMillis(new Date().getTime() + 1_296_000_000);

        String maxCalendarDate = getFormattedDate(calendar);

        request.setAttribute("minCalendarDate", minCalendarDate);
        request.setAttribute("maxCalendarDate", maxCalendarDate);
        log.debug("SearchTrainService execute()");

        return "/WEB-INF/user/searchToPurchase.jsp";
    }

    private List<Train> getTrainList() {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        List<Train> trains = new ArrayList<>();

        try (TrainDao trainDao = daoFactory.createTrainDao()) {
            trains = trainDao.findAll();

            log.debug("Search all trains");
        } catch (Exception e) {
            log.debug("Exception while getTrainList()");
            log.error("Exception while getTrainList() {}" + Arrays.toString(e.getStackTrace()));
        }

        return trains;
    }

    private String getFormattedDate(Calendar calendar) {
        return new StringBuilder()
                .append(calendar.get(Calendar.YEAR))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.MONTH) + 1))
                .append('-')
                .append(setDateZeroCharacter(calendar.get(Calendar.DATE))).toString();
    }

    private void setTravelDate(HttpServletRequest request, List<Train> trains) {
        String travelDate = request.getParameter("tripStartDate");
        Calendar calendar = Calendar.getInstance();
        String[] datesYMD = travelDate.split("-");

        calendar.set(Calendar.YEAR, Integer.parseInt(datesYMD[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(datesYMD[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datesYMD[2]));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        for (Train train : trains) {
            Timestamp firstStationDepartureTime = train.getDepartureRoute().getDepartureTime();
            train.getStations().forEach(route -> {
                Calendar arrivalTime = Calendar.getInstance();
                Calendar departureTime = Calendar.getInstance();

                arrivalTime.setTimeInMillis(route.getArrivalTime().getTime());
                departureTime.setTimeInMillis(route.getDepartureTime().getTime());

                long arrivalTimeDiffer = arrivalTime.getTimeInMillis() - firstStationDepartureTime.getTime();
                long departureTimeDiffer = departureTime.getTimeInMillis() - firstStationDepartureTime.getTime();

                route.setArrivalTime(new Timestamp(calendar.getTimeInMillis() + arrivalTimeDiffer));
                route.setDepartureTime(new Timestamp(calendar.getTimeInMillis() + departureTimeDiffer));
            });
        }
    }

    private String setDateZeroCharacter(int date) {
        if (date < 10) {
            return "0" + date;
        }
        return Integer.toString(date);
    }
}
