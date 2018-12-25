package ua.training.controller.command;

import ua.training.model.service.UserListManagingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserListCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return new UserListManagingService().execute(request);
    }
}
