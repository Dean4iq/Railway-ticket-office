package ua.training.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminFilter implements Filter {
    private static final Logger log = LogManager.getLogger(AdminFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("AdminFilter init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(true);

        if (session.getAttribute("Admin") == null) {
            log.debug("AdminFilter doFilter() loginRequest");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            log.debug("AdminFilter doFilter() doFilter");
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        log.debug("AdminFilter destroy()");
    }
}
