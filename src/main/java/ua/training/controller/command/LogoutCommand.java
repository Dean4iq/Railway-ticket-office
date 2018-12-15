package ua.training.controller.command;

import ua.training.model.service.LogoutService;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new LogoutService().execute(request);
    }
}
