package ua.training.controller.command;

import ua.training.model.service.SearchTrainService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return new SearchTrainService().execute(request);
    }
}
