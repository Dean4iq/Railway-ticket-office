package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class UserService implements Service {
    private static final Logger log = LogManager.getLogger(UserService.class);

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("userName", request.getSession().getAttribute("User"));
        log.debug("UserService execute()");
        return "/WEB-INF/user/user.jsp";
    }
}
