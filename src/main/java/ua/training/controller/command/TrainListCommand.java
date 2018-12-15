package ua.training.controller.command;

import ua.training.model.service.TrainListManagingService;

import javax.servlet.http.HttpServletRequest;

public class TrainListCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new TrainListManagingService().execute(request);
    }
}
