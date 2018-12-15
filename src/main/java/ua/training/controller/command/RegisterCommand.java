package ua.training.controller.command;

import ua.training.model.service.RegisterService;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new RegisterService().execute(request);
    }
}
