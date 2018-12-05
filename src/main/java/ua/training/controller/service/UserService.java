package ua.training.controller.service;

import javax.servlet.http.HttpServletRequest;

public class UserService implements Service {
    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("userName", request.getSession().getAttribute("User"));
        return "/WEB-INF/user/user.jsp";
    }
}
