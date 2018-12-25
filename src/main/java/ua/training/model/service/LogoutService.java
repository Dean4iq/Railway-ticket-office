package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class LogoutService implements Service {
    private static final Logger log = LogManager.getLogger(LogoutService.class);

    @Override
    public String execute(HttpServletRequest request) {
        if (request.getSession().getAttribute("User") != null) {
            String userName = (String)request.getSession().getAttribute("User");
            Set<String> loggedUsers = (Set<String>)request.getServletContext().getAttribute("loggedUsers");

            request.getSession().removeAttribute("User");
            loggedUsers.remove(userName);
        } else if (request.getSession().getAttribute("Admin") != null) {
            String userName = (String)request.getSession().getAttribute("Admin");
            Set<String> loggedUsers = (Set<String>)request.getServletContext().getAttribute("loggedUsers");

            request.getSession().removeAttribute("Admin");
            loggedUsers.remove(userName);
        }

        request.getSession().invalidate();

        log.debug("LogoutService class execute()");
        return "redirect: /";
    }
}
