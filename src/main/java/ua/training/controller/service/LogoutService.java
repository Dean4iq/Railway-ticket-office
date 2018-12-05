package ua.training.controller.service;

import javax.servlet.http.HttpServletRequest;

public class LogoutService implements Service {
    @Override
    public String execute(HttpServletRequest request) {
        if (request.getSession().getAttribute("User") != null) {
            request.getSession().removeAttribute("User");
        } else if (request.getSession().getAttribute("Admin") != null) {
            request.getSession().removeAttribute("Admin");
        }

        request.getSession().invalidate();

        return "redirect: /";
    }
}
