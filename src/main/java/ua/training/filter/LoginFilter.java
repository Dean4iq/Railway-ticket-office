package ua.training.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(true);

        String loginURI = req.getContextPath() + "/login";

        boolean loggedIn = session != null &&
                (session.getAttribute("User") != null || session.getAttribute("Admin") != null);
        boolean loginRequest = req.getRequestURI().equals(loginURI);

        if (loggedIn && loginRequest) {
            System.out.println("chillout");
            res.sendRedirect(req.getContextPath() + "/exception");
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
