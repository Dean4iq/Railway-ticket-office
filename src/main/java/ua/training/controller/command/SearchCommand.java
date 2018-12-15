package ua.training.controller.command;

import ua.training.model.service.SearchTrainService;

import javax.servlet.http.HttpServletRequest;

public class SearchCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new SearchTrainService().execute(request);
    }
}
