package ua.training.controller.service;

import javax.servlet.http.HttpServletRequest;

public class LogoutService implements Service {
    @Override
    public String execute(HttpServletRequest request) {
        return "/index.jsp";
    }
}
