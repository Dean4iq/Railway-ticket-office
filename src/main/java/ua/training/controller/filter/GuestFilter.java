package ua.training.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GuestFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(UserFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.debug("init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(true);

        if (session.getAttribute("User") != null
                || session.getAttribute("Admin") != null) {
            LOG.warn("doFilter() access denied to authorized user");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
        } else {
            LOG.debug("doFilter()");
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        LOG.debug("UserFilter class destroy()");
    }
}
