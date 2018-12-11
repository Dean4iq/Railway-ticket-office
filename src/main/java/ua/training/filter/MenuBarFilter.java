package ua.training.filter;

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

        if (session.getAttribute("User") == null && session.getAttribute("Admin") == null) {
            menuItems.put("Login", "login");
            menuItems.put("Register", "register");
        } else if (session.getAttribute("User") != null) {
            menuItems.put("Придбати квитки", "searchToPurchase");
            menuItems.put("Logout", "logout");
        } else {
            menuItems.put("Train list", "trainList");
            menuItems.put("User list", "userList");
            menuItems.put("Logout", "logout");
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
