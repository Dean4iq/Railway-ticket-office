package ua.training.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.service.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Servlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(Servlet.class);

    private final Map<String, Service> commands = new HashMap<>();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        servletConfig.getServletContext().setAttribute("loggedUsers", new HashSet<String>());

        commands.put("exception", new ExceptionService());
        commands.put("login", new LoginService());
        commands.put("user", new UserService());
        commands.put("admin", new AdminService());
        commands.put("logout", new LogoutService());
        commands.put("purchase", new PurchaseService());
        commands.put("register", new RegisterService());
        commands.put("search", new SearchService());
        commands.put("trainList", new TrainListManagingService());
        commands.put("userList", new UserListManagingService());
        commands.put("wagonReviewing", new WagonReviewingService());

        log.debug("Servlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setHeader("Cache-Control","no-cache");
            response.setHeader("Cache-Control","no-store");
            response.setDateHeader("Expires", 0);
            response.setHeader("Pragma","no-cache");

            log.debug("Servlet doGet");
            processRequest(request, response);
        } catch (Exception ex) {
            log.error(Arrays.toString(ex.getStackTrace()));
            response.sendRedirect(request.getContextPath() + "/exception");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            log.debug("Servlet doPost");
            processRequest(request, response);
        } catch (Exception ex) {
            log.error(Arrays.toString(ex.getStackTrace()));
            response.sendRedirect(request.getContextPath() + "/exception");
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        path = path.replaceAll(".*/zhdUA/", "");
        Service service = commands.getOrDefault(path,
                r -> "/index.jsp");
        String page = service.execute(request);

        if (page.contains("redirect: ")) {
            log.debug("Servlet redirect to " + page);
            response.sendRedirect(request.getContextPath() + page.replaceAll("redirect: ", ""));
        } else {
            log.debug("Servlet forward to " + page);
            request.getRequestDispatcher(page).forward(request,response);
        }
    }
}
