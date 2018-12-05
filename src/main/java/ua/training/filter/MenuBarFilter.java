package ua.training.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuBarFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

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
            menuItems.put("Logout", "logout");
        } else {
            menuItems.put("Logout", "logout");
        }

        req.setAttribute("userbar", menuItems);
        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
