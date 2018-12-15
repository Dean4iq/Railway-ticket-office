package ua.training.controller.command;

import ua.training.model.service.WagonReviewingService;

import javax.servlet.http.HttpServletRequest;

public class WagonReviewingCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new WagonReviewingService().execute(request);
    }
}
