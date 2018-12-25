package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.service.LogoutService;
import ua.training.util.LocalizationGetter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LogoutCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LogoutService.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession().getAttribute("User") != null) {
            String userName = (String) request.getSession().getAttribute("User");
            Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");

            request.getSession().removeAttribute("User");
            loggedUsers.remove(userName);
        } else if (request.getSession().getAttribute("Admin") != null) {
            String userName = (String) request.getSession().getAttribute("Admin");
            Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");

            request.getSession().removeAttribute("Admin");
            loggedUsers.remove(userName);
        }

        request.getSession().invalidate();

        LOG.debug("LogoutService class execute()");
        return "redirect: /";
    }
}
