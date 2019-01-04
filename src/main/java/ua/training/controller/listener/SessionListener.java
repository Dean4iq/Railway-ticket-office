package ua.training.controller.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.service.PurchaseService;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class SessionListener implements HttpSessionListener {
    public static final Logger LOG = LogManager.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Map<String, String> menuItems = new LinkedHashMap<>();

        menuItems.put("btn.login", "login");
        menuItems.put("btn.register", "register");

        Set<HttpSession> sessionSet = (Set<HttpSession>) httpSessionEvent
                .getSession().getServletContext().getAttribute("activeSessions");
        sessionSet.add(httpSessionEvent.getSession());
        httpSessionEvent.getSession().setAttribute("userbar", menuItems);

        LOG.info("New session created");
        LOG.debug("sessionCreated()");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HashSet<String> loggedUsers = (HashSet<String>) httpSessionEvent
                .getSession().getServletContext().getAttribute("loggedUsers");
        Set<HttpSession> sessionSet = (Set<HttpSession>) httpSessionEvent
                .getSession().getServletContext().getAttribute("activeSessions");

        checkTransactions(httpSessionEvent);
        sessionSet.remove(httpSessionEvent.getSession());

        if (httpSessionEvent.getSession().getAttribute("User") != null) {
            String userName = (String) httpSessionEvent.getSession()
                    .getAttribute("User");
            loggedUsers.remove(userName);

            LOG.info("User {} has logged out", userName);
        } else if (httpSessionEvent.getSession().getAttribute("Admin") != null) {
            String userName = (String) httpSessionEvent.getSession()
                    .getAttribute("Admin");
            loggedUsers.remove(userName);

            LOG.info("Admin {} has logged out", userName);
        }

        LOG.debug("sessionDestroyed()");
    }

    private void checkTransactions(HttpSessionEvent httpSessionEvent){
        if (httpSessionEvent.getSession().getAttribute("Ticket") != null
                && httpSessionEvent.getSession().getAttribute("ticketConnection") != null) {

            try (Connection connection =
                         (Connection) httpSessionEvent.getSession().getAttribute("ticketConnection")) {
                if (!connection.isClosed()) {
                    PurchaseService.declinePurchasing(connection);
                }
            } catch (SQLException exception) {
                LOG.error("Exception in sessionDestroyed(): {}", exception);
            }
        }
    }
}
