package ua.training.controller.command;

import ua.training.model.service.UserListManagingService;

import javax.servlet.http.HttpServletRequest;

public class UserListCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new UserListManagingService().execute(request);
    }
}
