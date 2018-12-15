package ua.training.controller.command;

import ua.training.model.service.LoginService;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new LoginService().execute(request);
    }
}
