package ua.training.controller;

import ua.training.controller.service.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Servlet extends HttpServlet {
    private Map<String, Service> commands = new HashMap<>();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        servletConfig.getServletContext().setAttribute("loggedUsers", new HashSet<String>());

        commands.put("exception", new ExceptionService());
        commands.put("login", new LoginService());
        commands.put("logout", new LogoutService());
        commands.put("purchase", new PurchaseService());
        commands.put("register", new RegisterService());
        commands.put("search", new SearchService());
        commands.put("trainsManaging", new TrainListManagingService());
        commands.put("usersManaging", new UserListManagingService());
        commands.put("wagonReviewing", new WagonReviewingService());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/exception");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            processRequest(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/exception");
            httpServletRequest.getRequestDispatcher("/error.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        path = path.replaceAll(".*/zhdUA/", "");
        Service service = commands.getOrDefault(path,
                r -> "/index.jsp");
        System.out.println(service.getClass().getName());
        String page = service.execute(request);
        request.getRequestDispatcher(page).forward(request, response);
    }
}
