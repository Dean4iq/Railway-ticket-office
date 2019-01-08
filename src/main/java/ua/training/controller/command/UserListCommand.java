package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.User;
import ua.training.model.service.UserListManagingService;
import ua.training.model.util.RegExSources;
import ua.training.model.util.RegExStringsGetter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

public class UserListCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(UserListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");

        checkActions(request);

        List<User> users = UserListManagingService.getUserList();
        request.setAttribute("userList", users);

        return "/WEB-INF/admin/userList.jsp";
    }

    private void checkActions(HttpServletRequest request) {
        LOG.debug("checkActions()");
        if (request.getParameter("actionPicker") != null) {
            processActions(request);
        } else if (request.getParameter("changeUserData") != null) {
            changeUserData(request);
        }
    }

    private void processActions(HttpServletRequest request) {
        LOG.debug("processActions()");
        String parameter = request.getParameter("actionPicker");

        if (parameter.contains("delete")) {
            String userLogin = parameter.substring("delete".length());
            User user = UserListManagingService.getUser(userLogin);
            if (user.checkLogin() && !user.isAdmin()) {
                UserListManagingService.deleteUser(user);
            }
        } else if (parameter.contains("update")) {
            String userLogin = parameter.substring("update".length());
            User user = UserListManagingService.getUser(userLogin);
            if (user.checkLogin() && !user.isAdmin()) {
                request.setAttribute("selectedUser", user);
                request.getSession().setAttribute("selectedUserLogin", user.getLogin());
            }
        }
    }

    private void changeUserData(HttpServletRequest request) {
        LOG.debug("changeUserData()");
        Map<String, String> userAttributes = new HashMap<>();

        userAttributes.put("name", request.getParameter("updateName"));
        userAttributes.put("lastName", request.getParameter("updateLastName"));
        userAttributes.put("nameUA", request.getParameter("updateNameUA"));
        userAttributes.put("lastNameUA", request.getParameter("updateLastNameUA"));
        userAttributes.put("login", (String) request.getSession().getAttribute("selectedUserLogin"));
        userAttributes.put("password", request.getParameter("updatePassword"));

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

        user.setName(userDataMap.get("name"));
        user.setLastName(userDataMap.get("lastName"));
        user.setNameUA(userDataMap.get("nameUA"));
        user.setLastNameUA(userDataMap.get("lastNameUA"));
        user.setLogin(userDataMap.get("login"));
        user.setPassword(userDataMap.get("password"));

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
                .getServletContext().getAttribute("activeSessions");

        sessions.stream().filter(httpSession ->
                httpSession.getAttribute("User").equals(login))
                .findFirst().ifPresent(HttpSession::invalidate);
    }
}
