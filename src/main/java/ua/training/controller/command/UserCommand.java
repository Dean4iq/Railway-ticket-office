package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(UserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("userName", request.getSession().getAttribute("User"));
        LOG.debug("execute()");
        return "/WEB-INF/user/user.jsp";
    }
}
