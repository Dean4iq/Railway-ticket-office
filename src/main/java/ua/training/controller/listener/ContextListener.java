package ua.training.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Set<String> activeUsers = new HashSet<>();
        Set<HttpSession> activeSessions = new HashSet<>();
        servletContextEvent.getServletContext().setAttribute("loggedUsers", activeUsers);
        servletContextEvent.getServletContext().setAttribute("activeSessions", activeSessions);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().removeAttribute("loggedUsers");
        servletContextEvent.getServletContext().removeAttribute("activeSessions");
    }

}
