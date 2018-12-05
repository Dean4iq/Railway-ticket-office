package ua.training.controller.service;

import javax.servlet.http.HttpServletRequest;

public class UserService implements Service {
    @Override
    public String execute(HttpServletRequest request) {
        return "/user.jsp";
    }
}
