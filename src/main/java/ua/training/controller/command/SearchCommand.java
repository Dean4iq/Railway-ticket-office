package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.service.MethodProvider;
import ua.training.model.service.SearchTrainService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SearchCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SearchCommand.class);
    private static final Map<String, MethodProvider> COMMANDS = new HashMap<>();

    public SearchCommand() {
        COMMANDS.putIfAbsent("trainNumberSubmit", SearchTrainService::findTrainById);
        COMMANDS.putIfAbsent("trainDestinationSubmit", SearchTrainService::findTrainByRoute);
        COMMANDS.putIfAbsent("allTrainSubmit", SearchTrainService::findAllTrains);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String parameter = params.nextElement();
            if (COMMANDS.containsKey(parameter)) {
                COMMANDS.get(parameter).call(request);
            }
        }

        SearchTrainService.setStationList(request);

        return "/searchTrains.jsp";
    }
}
