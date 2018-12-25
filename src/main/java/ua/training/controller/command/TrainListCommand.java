package ua.training.controller.command;

import ua.training.model.service.TrainListManagingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrainListCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return new TrainListManagingService().execute(request);
    }
}
