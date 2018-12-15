package ua.training.controller.command;

import ua.training.model.service.AdminService;

import javax.servlet.http.HttpServletRequest;

public class AdminCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new AdminService().execute(request);
    }
}
