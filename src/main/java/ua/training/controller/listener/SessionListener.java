package ua.training.controller.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.service.PurchaseService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.ServletContext;
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
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Map<String, String> menuItems = new LinkedHashMap<>();
        ServletContext context = httpSessionEvent.getSession().getServletContext();

        menuItems.put("btn.login", "login");
        menuItems.put("btn.register", "register");

        Set<HttpSession> sessionSet = (Set<HttpSession>) context
                .getAttribute(attrManager.getString(AttributeSources.ACTIVE_SESSIONS));
        sessionSet.add(httpSessionEvent.getSession());
        httpSessionEvent.getSession()
                .setAttribute(attrManager.getString(AttributeSources.USERBAR), menuItems);

        LOG.info("New session created");
        LOG.debug("sessionCreated()");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        ServletContext context = session.getServletContext();

        HashSet<String> loggedUsers = (HashSet<String>) context
                .getAttribute(attrManager.getString(AttributeSources.LOGGED_USERS_CONTEXT));
        Set<HttpSession> sessionSet = (Set<HttpSession>) context
                .getAttribute(attrManager.getString(AttributeSources.ACTIVE_SESSIONS));

        checkTransactions(httpSessionEvent);
        sessionSet.remove(httpSessionEvent.getSession());

        if (session.getAttribute(attrManager.getString(AttributeSources.ROLE_USER)) != null) {
            String userName = (String) session
                    .getAttribute(attrManager.getString(AttributeSources.ROLE_USER));
            loggedUsers.remove(userName);

            LOG.info("User's '{}' session destroyed", userName);
        } else if (session.getAttribute(attrManager.getString(AttributeSources.ROLE_ADMIN)) != null) {
            String userName = (String) session
                    .getAttribute(attrManager.getString(AttributeSources.ROLE_ADMIN));
            loggedUsers.remove(userName);

            LOG.info("Admin's '{}' session destroyed", userName);
        }

        LOG.debug("sessionDestroyed()");
    }

    private void checkTransactions(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        if (session.getAttribute(attrManager.getString(AttributeSources.TICKET_PURCHASE)) != null
                && session.getAttribute(attrManager.getString(AttributeSources.TICKET_CONNECTION)) != null) {

            try (Connection connection = (Connection) session
                    .getAttribute(attrManager.getString(AttributeSources.TICKET_CONNECTION))) {
                if (!connection.isClosed()) {
                    PurchaseService.declinePurchasing(connection);
                }
            } catch (SQLException exception) {
                LOG.error("Exception in sessionDestroyed(): {}", exception);
            }
        }
    }
}
