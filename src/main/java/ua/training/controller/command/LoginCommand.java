package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.exception.InvalidLoginOrPasswordException;
import ua.training.exception.LoggedInLoginException;
import ua.training.exception.NotExistedLoginException;
import ua.training.model.entity.User;
import ua.training.model.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LoginCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");

        User user = new User();

        String username = request.getParameter("login");
        String password = request.getParameter("pass");

        user.setLogin(username);
        user.setPassword(password);

        try {
            if (user.checkLogin() && user.checkPassword()
                    && LoginService.checkLoginExistence(user)
                    && checkUserSession(request, user)) {
                user = LoginService.checkLoginPassword(user);
                if (user.isAdmin()) {
                    processAdmin(request, user);
                    return "redirect: /admin";
                } else {
                    processUser(request, user);
                    return "redirect: /user";
                }
            }
        } catch (NotExistedLoginException e) {
            request.setAttribute("notExistedLogin", true);

            LOG.warn("Someone tried to log in using non-existed username: {}", e.getMessage());
        } catch (InvalidLoginOrPasswordException e) {
            request.setAttribute("invalidLogin", true);
            LOG.warn("Someone tried to log in: {}", e.getMessage());
        } catch (LoggedInLoginException e) {
            request.setAttribute("loggedLogin", true);
            LOG.warn("Someone tried to log in again: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
            return "redirect: /exception";
        }

        return "/login.jsp";
    }

    private boolean checkUserSession(HttpServletRequest request, User user)
            throws LoggedInLoginException {
        Set<String> users = (Set<String>) request.getServletContext().getAttribute("loggedUsers");
        if (users.stream().noneMatch(loggedUser ->
                user.getLogin().equals(loggedUser))) {
            return true;
        }
        throw new LoggedInLoginException(user.getLogin());
    }

    private void processAdmin(HttpServletRequest request, User user) {
        Map<String, String> menuItems = new LinkedHashMap<>();

        request.getSession().setAttribute("Admin", user.getLogin());
        addUserToServletContext(request, user);

        request.getSession().setMaxInactiveInterval(0);

        LOG.debug("Admin in LoginService class execute()");
        LOG.info("Admin {} has successfully logged in", user.getLogin());

        menuItems.put("btn.trainList", "trains");
        menuItems.put("btn.userList", "users");
        menuItems.put("btn.logout", "logout");
        request.getSession().setAttribute("userbar", menuItems);
    }

    private void processUser(HttpServletRequest request, User user) {
        Map<String, String> menuItems = new LinkedHashMap<>();

        request.getSession(true).setAttribute("User", user.getLogin());
        addUserToServletContext(request, user);

        LOG.debug("User in LoginService class execute()");
        LOG.info("User {} has successfully logged in", user.getLogin());

        menuItems.put("btn.purchaseTicket", "tickets");
        menuItems.put("btn.logout", "logout");

        request.getSession().setAttribute("userbar", menuItems);
    }

    private void addUserToServletContext(HttpServletRequest request, User user) {
        Set<String> users = (Set<String>) request.getServletContext().getAttribute("loggedUsers");
        users.add(user.getLogin());
    }
}
