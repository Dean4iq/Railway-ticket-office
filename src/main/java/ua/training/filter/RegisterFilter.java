package ua.training.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterFilter implements Filter {
    private final Logger log = LogManager.getLogger(RegisterFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(true);

        String registerURI = httpRequest.getContextPath() + "/register";

        boolean loggedIn = (session != null &&
                (session.getAttribute("User") != null
                        || session.getAttribute("Admin") != null));
        boolean loginRequest = httpRequest.getRequestURI().equals(registerURI);

        if (loggedIn && loginRequest) {
            StringBuilder stringBuilder = new StringBuilder();

            if (session.getAttribute("User") != null) {
                stringBuilder.append("Authorized user '")
                        .append(session.getAttribute("User"))
                        .append("' tried to register");
            } else {

                stringBuilder.append("Authorized user '")
                        .append(session.getAttribute("Admin"))
                        .append("' tried to register");
            }
            log.warn(stringBuilder.toString());

            res.sendRedirect(httpRequest.getContextPath() + "/");
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
