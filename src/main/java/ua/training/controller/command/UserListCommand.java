package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.User;
import ua.training.model.service.UserListManagingService;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;
import ua.training.model.util.RegExSources;
import ua.training.model.util.RegExStringsGetter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

public class UserListCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(UserListCommand.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");

        checkActions(request);

        List<User> users = UserListManagingService.getUserList();
        request.setAttribute(attrManager.getString(AttributeSources.USER_USERLIST), users);

        return "/WEB-INF/admin/userList.jsp";
    }

    private void checkActions(HttpServletRequest request) {
        LOG.debug("checkActions()");

        if (request.getParameter(attrManager.getString(AttributeSources.USER_CHANGE_PARAM)) != null) {
            processActions(request);
        } else if (request.getParameter(attrManager
                .getString(AttributeSources.USER_ACTION_UPDATE_PARAM)) != null) {
            changeUserData(request);
        }
    }

    private void processActions(HttpServletRequest request) {
        LOG.debug("processActions()");
        String parameter = request.getParameter(attrManager.getString(AttributeSources.USER_CHANGE_PARAM));

        if (parameter.contains(attrManager.getString(AttributeSources.USER_DELETE_PARAM))) {
            String userLogin = parameter.substring(attrManager
                    .getString(AttributeSources.USER_DELETE_PARAM).length());
            User user = UserListManagingService.getUser(userLogin);
            if (user.checkLogin() && !user.isAdmin()) {
                UserListManagingService.deleteUser(user);
            }
        } else if (parameter.contains(attrManager.getString(AttributeSources.USER_UPDATE_PARAM))) {
            String userLogin = parameter.substring(attrManager
                    .getString(AttributeSources.USER_UPDATE_PARAM).length());
            User user = UserListManagingService.getUser(userLogin);
            if (user.checkLogin() && !user.isAdmin()) {
                request.setAttribute(attrManager.getString(AttributeSources.USER_SELECTED), user);
                request.getSession().setAttribute(attrManager
                        .getString(AttributeSources.USER_SELECTED_LOGIN), user.getLogin());
            }
        }
    }

    private void changeUserData(HttpServletRequest request) {
        LOG.debug("changeUserData()");
        Map<String, String> userAttributes = new HashMap<>();
        HttpSession session = request.getSession();

        userAttributes.put(attrManager.getString(AttributeSources.NAME_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.USER_UPDATE_NAME_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.LASTNAME_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.USER_UPDATE_LASTNAME_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.NAME_UA_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.USER_UPDATE_NAME_UA_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.LASTNAME_UA_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.USER_UPDATE_LASTNAME_UA_PARAM)));
        userAttributes.put(attrManager.getString(AttributeSources.LOGIN_PARAM),
                (String) session.getAttribute(attrManager.getString(AttributeSources.USER_SELECTED_LOGIN)));
        userAttributes.put(attrManager.getString(AttributeSources.PASSWORD_REG_PARAM),
                request.getParameter(attrManager.getString(AttributeSources.USER_UPDATE_PASS_PARAM)));

        if (userAttributes.entrySet().stream()
                .noneMatch(elem -> elem.getValue() == null
                        || elem.getValue().equals(""))) {

            User user = setUserData(userAttributes);

            if (checkAllFields(userAttributes, request)) {
                UserListManagingService.updateUser(user);
                endUserSession(request, user.getLogin());
            }
        }
    }

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

    private boolean checkFieldRegEx(String field, String regexKey) {
        String regexString = new RegExStringsGetter().getRegExString(regexKey);
        return (field.matches(regexString));
    }

    private void endUserSession(HttpServletRequest request, String login) {
        Set<HttpSession> sessions = (Set<HttpSession>) request
                .getServletContext().getAttribute(attrManager.getString(AttributeSources.ACTIVE_SESSIONS));

        sessions.stream().filter(httpSession ->
                httpSession.getAttribute(attrManager.getString(AttributeSources.ROLE_USER)).equals(login))
                .findFirst().ifPresent(HttpSession::invalidate);
    }
}
