package ua.training.controller.command;

import ua.training.model.service.ExceptionService;

import javax.servlet.http.HttpServletRequest;

public class ExceptionCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new ExceptionService().execute(request);
    }
}
