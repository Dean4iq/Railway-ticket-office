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
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class {@code LoginCommand} provide methods to authenticate user or admin in
 * the system.
 * Class includes multiple methods for validating inputted strings.
 *
 * @author Dean4iq
 * @version 1.0
 * @see LoginService
 */
public class LoginCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);
    private AttributeResourceManager attributeManager = AttributeResourceManager.INSTANCE;

    /**
     * Process user request for login
     *
     * @param request provides user data to process and link to session and context
     * @return link to users page according to the role or link to the login page
     */
    @Override
    public String execute(HttpServletRequest request) {
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

    /**
     * Checks if user is in system now.
     * Method uses the user set, stored in a servlet context.
     *
     * @param request provides link to context
     * @param user A user who want to log in the system
     * @return true if the user is not in the system yet
     * @throws LoggedInLoginException if user already logged in system.
     */
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

    /**
     * If the user is an admin then he should be signed in as admin.
     * An authorized user is written to the user set in a servlet context and in the user session.
     * Admin has endless inactive interval.
     *
     * @param request provides link to session and context
     * @param user entity to log
     */
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

    /**
     * If the user is some common user then he should be signed in as user.
     * An authorized user is written to the user set in a servlet context and in the user session.
     *
     * @param request provides link to session and context
     * @param user entity to log
     */
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

    /**
     * Every active user session should be written in context to avoid multiple
     * sessions for the same username.
     *
     * @param request provides link to context
     * @param user entity for being written in the context
     */
    private void addUserToServletContext(HttpServletRequest request, User user) {
        Set<String> users = (Set<String>) request.getServletContext()
                .getAttribute(attributeManager.getString(AttributeSources.LOGGED_USERS_CONTEXT));
        users.add(user.getLogin());
    }
}
