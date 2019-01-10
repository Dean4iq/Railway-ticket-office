package ua.training.controller.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class ContextListener implements ServletContextListener {
    public static final Logger LOG = LogManager.getLogger(ContextListener.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Set<String> activeUsers = new HashSet<>();
        Set<HttpSession> activeSessions = new HashSet<>();

        servletContextEvent.getServletContext()
                .setAttribute(attrManager.getString(AttributeSources.LOGGED_USERS_CONTEXT), activeUsers);
        servletContextEvent.getServletContext()
                .setAttribute(attrManager.getString(AttributeSources.ACTIVE_SESSIONS), activeSessions);

        LOG.debug("Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext()
                .removeAttribute(attrManager.getString(AttributeSources.LOGGED_USERS_CONTEXT));
        servletContextEvent.getServletContext()
                .removeAttribute(attrManager.getString(AttributeSources.ACTIVE_SESSIONS));

        LOG.debug("Destroyed");
    }

}
