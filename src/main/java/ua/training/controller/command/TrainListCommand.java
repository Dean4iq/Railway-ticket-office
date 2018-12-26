package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Train;
import ua.training.model.service.TrainListManagingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class TrainListCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(TrainListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("TrainListManagingService execute()");

        checkActions(request);

        List<Train> trains = getTrains();
        request.setAttribute("trainList", trains);

        return "/WEB-INF/admin/trainList.jsp";
    }

    private void checkActions(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            if (parameter.contains("del")) {
                processActions(parameter);
            }
        }
    }

    private void processActions(String parameter) {
        if (parameter.contains("del")) {
            int trainId = Integer.parseInt(parameter.substring("del".length()));
            TrainListManagingService.deleteTrain(trainId);
        }
    }

    private List<Train> getTrains() {
        List<Train> trainList = TrainListManagingService.getTrainList();
        List<Train> trains = TrainListManagingService.getAllTrainList();

        trains = trains.stream().filter(train ->
                trainList.stream().noneMatch(allTrains ->
                        allTrains.getId() == train.getId())).collect(Collectors.toList());

        trainList.addAll(trains);

        return trainList;
    }
}
