package ua.training.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    private static final Logger log = LogManager.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("LoginFilter init()");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(true);

        String loginURI = req.getContextPath() + "/login";

        boolean loggedIn = (session != null &&
                (session.getAttribute("User") != null
                        || session.getAttribute("Admin") != null));
        boolean loginRequest = req.getRequestURI().equals(loginURI);

        if (loggedIn && loginRequest) {
            StringBuilder stringBuilder = new StringBuilder();

            if (session.getAttribute("User") != null) {
                stringBuilder.append("Authorized user '")
                        .append(session.getAttribute("User"))
                        .append("' tried to log in");
            } else {

                stringBuilder.append("Authorized user '")
                        .append(session.getAttribute("Admin"))
                        .append("' tried to log in");
            }
            log.warn(stringBuilder.toString());

            log.debug("LoginFilter doFilter() access denied");
            res.sendRedirect(req.getContextPath() + "/");
        } else {
            log.debug("LoginFilter doFilter() doFilter");
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        log.debug("LoginFilter destroy()");
    }
}
