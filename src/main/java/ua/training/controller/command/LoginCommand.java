package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.exception.InvalidLoginOrPasswordException;
import ua.training.exception.LoggedInLoginException;
import ua.training.exception.NotExistedLoginException;
import ua.training.model.entity.User;
import ua.training.model.service.LoginService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LoginCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);
    private AttributeResourceManager attributeManager = AttributeResourceManager.INSTANCE;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");

        User user = new User();

        String username = request.getParameter(attributeManager.getString(AttributeSources.LOGIN_PARAM));
        String password = request.getParameter(attributeManager.getString(AttributeSources.PASSWORD_PARAM));

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
            request.setAttribute(attributeManager.getString(AttributeSources.LOGIN_NOT_EXISTED),
                    true);

            LOG.warn("Someone tried to log in using non-existed username: {}", e.getMessage());
        } catch (InvalidLoginOrPasswordException e) {
            request.setAttribute(attributeManager.getString(AttributeSources.INVALID_LOGIN),
                    true);
            LOG.warn("Someone tried to log in: {}", e.getMessage());
        } catch (LoggedInLoginException e) {
            request.setAttribute(attributeManager.getString(AttributeSources.ALREADY_LOGGED_IN),
                    true);
            LOG.warn("Someone tried to log in again: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Exception {}", e);
            return "redirect: /exception";
        }

        return "/login.jsp";
    }

    private boolean checkUserSession(HttpServletRequest request, User user)
            throws LoggedInLoginException {
        ServletContext context = request.getServletContext();
        Set<String> users = (Set<String>) context
                .getAttribute(attributeManager.getString(AttributeSources.LOGGED_USERS_CONTEXT));

        if (users.stream().noneMatch(loggedUser -> user.getLogin().equals(loggedUser))) {
            return true;
        }
        throw new LoggedInLoginException(user.getLogin());
    }

    private void processAdmin(HttpServletRequest request, User user) {
        Map<String, String> menuItems = new LinkedHashMap<>();
        HttpSession session = request.getSession();

        session.setAttribute(attributeManager.getString(AttributeSources.ROLE_ADMIN),
                user.getLogin());
        addUserToServletContext(request, user);

        session.setMaxInactiveInterval(0); //Endless inactive interval

        LOG.debug("Admin in LoginService class execute()");
        LOG.info("Admin {} has successfully logged in", user.getLogin());

        menuItems.put("btn.trainList", "trains");
        menuItems.put("btn.userList", "users");
        menuItems.put("btn.logout", "logout");
        session.setAttribute(attributeManager.getString(AttributeSources.USERBAR),
                menuItems);
    }

    private void processUser(HttpServletRequest request, User user) {
        Map<String, String> menuItems = new LinkedHashMap<>();
        HttpSession session = request.getSession();

        session.setAttribute(attributeManager.getString(AttributeSources.ROLE_USER),
                user.getLogin());
        addUserToServletContext(request, user);

        LOG.debug("User in LoginService class execute()");
        LOG.info("User {} has successfully logged in", user.getLogin());

        menuItems.put("btn.purchaseTicket", "tickets");
        menuItems.put("btn.logout", "logout");

        session.setAttribute(attributeManager.getString(AttributeSources.USERBAR),
                menuItems);
    }

    private void addUserToServletContext(HttpServletRequest request, User user) {
        Set<String> users = (Set<String>) request.getServletContext()
                .getAttribute(attributeManager.getString(AttributeSources.LOGGED_USERS_CONTEXT));
        users.add(user.getLogin());
    }
}
