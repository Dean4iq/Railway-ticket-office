package ua.training.controller.command;

import ua.training.model.service.WagonReviewingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WagonReviewingCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return new WagonReviewingService().execute(request);
    }
}
