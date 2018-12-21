package ua.training.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserFilter implements Filter {
    private static final Logger log = LogManager.getLogger(UserFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("UserFilter class init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(true);

        if (session.getAttribute("User") == null) {
            log.debug("UserFilter class doFilter() access denied");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            log.debug("UserFilter class doFilter()");
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        log.debug("UserFilter class destroy()");
    }
}
