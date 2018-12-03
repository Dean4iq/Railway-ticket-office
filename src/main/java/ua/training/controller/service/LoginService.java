package ua.training.controller.service;

import javax.servlet.http.HttpServletRequest;

public class LoginService implements Service {
    @Override
    public String execute(HttpServletRequest request) {
        return "/login.jsp";
    }
}
