package ua.training.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuBarFilter implements Filter {
    private static final Logger log = LogManager.getLogger(MenuBarFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("MenuBarFilter init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Map<String, String> menuItems = new LinkedHashMap<>();
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(true);
        Map<String, String> localizationStrings = (Map<String, String>) session.getAttribute("mapValues");

        if (session.getAttribute("User") == null && session.getAttribute("Admin") == null) {
            menuItems.put(localizationStrings.get("btn.login"), "login");
            menuItems.put(localizationStrings.get("btn.register"), "register");
        } else if (session.getAttribute("User") != null) {
            menuItems.put(localizationStrings.get("btn.purchaseTicket"), "tickets");
            menuItems.put(localizationStrings.get("btn.logout"), "logout");
        } else {
            menuItems.put(localizationStrings.get("btn.trainList"), "trains");
            menuItems.put(localizationStrings.get("btn.userList"), "users");
            menuItems.put(localizationStrings.get("btn.logout"), "logout");
        }

        req.setAttribute("userbar", menuItems);

        log.debug("MenuBarFilter doFilter()");
        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        log.debug("MenuBarFilter destroy()");
    }
}
