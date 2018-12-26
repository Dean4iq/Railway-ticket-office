package ua.training.controller.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Map<String, String> menuItems = new LinkedHashMap<>();

        menuItems.put("btn.login", "login");
        menuItems.put("btn.register", "register");

        Set<HttpSession> sessionSet = (Set<HttpSession>) httpSessionEvent
                .getSession().getServletContext().getAttribute("activeSessions");
        sessionSet.add(httpSessionEvent.getSession());
        httpSessionEvent.getSession().setAttribute("userbar", menuItems);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HashSet<String> loggedUsers = (HashSet<String>) httpSessionEvent
                .getSession().getServletContext().getAttribute("loggedUsers");
        Set<HttpSession> sessionSet = (Set<HttpSession>) httpSessionEvent
                .getSession().getServletContext().getAttribute("activeSessions");
        sessionSet.add(httpSessionEvent.getSession());

        sessionSet.remove(httpSessionEvent.getSession());

        if (httpSessionEvent.getSession().getAttribute("User") != null) {
            String userName = (String) httpSessionEvent.getSession()
                    .getAttribute("User");
            loggedUsers.remove(userName);
        } else if (httpSessionEvent.getSession().getAttribute("Admin") != null) {
            String userName = (String) httpSessionEvent.getSession()
                    .getAttribute("Admin");
            loggedUsers.remove(userName);
        }

        httpSessionEvent.getSession().setAttribute("loggedUsers", loggedUsers);
    }
}
