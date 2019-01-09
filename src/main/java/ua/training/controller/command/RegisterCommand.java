package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.exception.NotUniqueLoginException;
import ua.training.model.entity.User;
import ua.training.model.service.RegisterService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;
import ua.training.model.util.RegExSources;
import ua.training.model.util.RegExStringsGetter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class {@code RegisterCommand} provides methods to sign up a new user in the
 * system
 *
 * @author Dean4iq
 * @version 1.0
 */
public class RegisterCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(RegisterCommand.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    /**
     * Gets all registration info strings and validates them
     *
     * @param request provides user data to process and link to session and context
     * @return link to register page or user page if registration process is
     *          successfully accomplished
     */
    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("execute()");
        Map<String, String> userAttributes = new HashMap<>();

        userAttributes.put(attrManager.getString(AttributeSources.NAME_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.NAME_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.LASTNAME_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.LASTNAME_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.NAME_UA_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.NAME_UA_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.LASTNAME_UA_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.LASTNAME_UA_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.LOGIN_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.LOGIN_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.PASSWORD_REG_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.PASSWORD_REG_PARAM)));

        if (userAttributes.entrySet().stream()
                .noneMatch(elem -> elem.getValue() == null
                        || elem.getValue().isEmpty())) {

            User user = setUserData(userAttributes);

            try {
                if (RegisterService.checkUniqueLogin(user)
                        && checkAllFields(userAttributes, request)) {
                    RegisterService.createNewUser(user);
                    LOG.info("New user {}", user.getLogin());

                    setUserSession(request, user);
                    LOG.debug("New user in RegisterService execute()");

                    return "redirect: /user";
                }
            } catch (NotUniqueLoginException e) {
                LOG.error("Not unique login", e.getMessage());
                request.setAttribute(attrManager.getString(AttributeSources.NOT_UNIQUE_LOGIN), true);
            }
        }

        return "/register.jsp";
    }

    /**
     * Puts all gained user info from register page to User object
     *
     * @param userDataMap map with values from register page
     * @return object User with completed fields with info from register page
     */
    private User setUserData(Map<String, String> userDataMap) {
        User user = new User();

        user.setName(userDataMap.get(attrManager.getString(AttributeSources.NAME_PARAM)));
        user.setLastName(userDataMap.get(attrManager.getString(AttributeSources.LASTNAME_PARAM)));
        user.setNameUA(userDataMap.get(attrManager.getString(AttributeSources.NAME_UA_PARAM)));
        user.setLastNameUA(userDataMap.get(attrManager.getString(AttributeSources.LASTNAME_UA_PARAM)));
        user.setLogin(userDataMap.get(attrManager.getString(AttributeSources.LOGIN_PARAM)));
        user.setPassword(userDataMap.get(attrManager.getString(AttributeSources.PASSWORD_REG_PARAM)));

        return user;
    }

    /**
     * If user has successfully signed up he should be signed in the system
     * For new user, there should be another menu bar
     *
     * @param request provides links to session and context
     * @param user signed up user
     */
    private void setUserSession(HttpServletRequest request, User user) {
        Map<String, String> menuItems = new LinkedHashMap<>();
        HttpSession session = request.getSession();
        ServletContext context = request.getServletContext();

        session.setAttribute(attrManager.getString(AttributeSources.ROLE_USER), user.getLogin());
        Set<String> loggedUsers = (Set<String>) context.getAttribute(attrManager
                .getString(AttributeSources.LOGGED_USERS_CONTEXT));
        loggedUsers.add(user.getLogin());

        menuItems.put("btn.purchaseTicket", "tickets");
        menuItems.put("btn.logout", "logout");

        request.getSession().setAttribute(attrManager.getString(AttributeSources.USERBAR), menuItems);
    }

    /**
     * Validates all user register data fields on regular expressions
     *
     * @param fields map with the user register data values
     * @param request if some fields are invalid, the warning should occur
     *               on the register page
     * @return true if all fields are valid, otherwise false
     */
    private boolean checkAllFields(Map<String, String> fields, HttpServletRequest request) {
        Map<String, String> resultedMap = fields.entrySet().stream().filter(elem -> {
            String regexKey = Arrays.stream(RegExSources.values()).filter(source ->
                    elem.getKey().equals(source.getField())).findFirst().get().getLink();
            if (!checkFieldRegEx(elem.getValue(), regexKey)) {
                request.setAttribute(elem.getKey() + "Invalid", true);
                return true;
            }
            return false;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return resultedMap.isEmpty();
    }

    /**
     * Gets regular expression for the regex key and validates field on regex
     *
     * @param field string that should be validated
     * @param regexKey key to regex string in property file
     * @return true if field is valid, otherwise false
     */
    private boolean checkFieldRegEx(String field, String regexKey) {
        String regexString = new RegExStringsGetter().getRegExString(regexKey);
        return (field.matches(regexString));
    }
}
