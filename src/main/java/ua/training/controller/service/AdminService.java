package ua.training.controller.service;

import javax.servlet.http.HttpServletRequest;

public class AdminService implements Service {
    @Override
    public String execute(HttpServletRequest request) {
        return "/WEB-INF/admin/admin.jsp";
    }
}
