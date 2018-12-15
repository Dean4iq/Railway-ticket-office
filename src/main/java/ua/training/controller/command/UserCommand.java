package ua.training.controller.command;

import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class UserCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new UserService().execute(request);
    }
}
