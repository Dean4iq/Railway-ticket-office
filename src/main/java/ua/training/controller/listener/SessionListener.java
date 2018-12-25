package ua.training.controller.listener;

import ua.training.util.LanguageISO;
import ua.training.util.LocalizationGetter;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;


public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Map<String, String> menuItems = new LinkedHashMap<>();

        menuItems.put("btn.login", "login");
        menuItems.put("btn.register", "register");

        httpSessionEvent.getSession().setAttribute("userbar", menuItems);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HashSet<String> loggedUsers = (HashSet<String>) httpSessionEvent
                .getSession().getServletContext()
                .getAttribute("loggedUsers");

        if (httpSessionEvent.getSession().getAttribute("User")!=null) {
            String userName = (String) httpSessionEvent.getSession()
                    .getAttribute("User");
            loggedUsers.remove(userName);
        } else if (httpSessionEvent.getSession().getAttribute("Admin")!=null){
            String userName = (String) httpSessionEvent.getSession()
                    .getAttribute("Admin");
            loggedUsers.remove(userName);
        }

        httpSessionEvent.getSession().setAttribute("loggedUsers", loggedUsers);
    }
}
