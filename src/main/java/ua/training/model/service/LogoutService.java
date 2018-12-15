package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class LogoutService implements Service {
    private static final Logger log = LogManager.getLogger(LogoutService.class);

    @Override
    public String execute(HttpServletRequest request) {
        if (request.getSession().getAttribute("User") != null) {
            request.getSession().removeAttribute("User");
        } else if (request.getSession().getAttribute("Admin") != null) {
            request.getSession().removeAttribute("Admin");
        }

        request.getSession().invalidate();

        log.debug("LogoutService class execute()");
        return "redirect: /";
    }
}
