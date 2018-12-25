package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.exception.NotUniqueLoginException;
import ua.training.model.entity.User;
import ua.training.model.service.RegisterService;
import ua.training.util.RegExSources;
import ua.training.util.RegExStringsGetter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RegisterCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("execute()");
        Map<String, String> userAttributes = new HashMap<>();

        userAttributes.put("name", request.getParameter("name"));
        userAttributes.put("lastName", request.getParameter("lastName"));
        userAttributes.put("nameUA", request.getParameter("nameUA"));
        userAttributes.put("lastNameUA", request.getParameter("lastNameUA"));
        userAttributes.put("login", request.getParameter("login"));
        userAttributes.put("password", request.getParameter("password"));

        if (userAttributes.entrySet().stream()
                .noneMatch(elem -> elem.getValue() == null
                        || elem.getValue().equals(""))) {

            User user = setUserData(userAttributes);

            try {
                if (RegisterService.checkUniqueLogin(user)
                        && checkAllFields(userAttributes, request)) {
                    System.out.println("BLYA");
                    RegisterService.createNewUser(user);
                    LOG.info("New user {}", user.getLogin());

                    setUserSession(request, user);
                    LOG.debug("New user in RegisterService execute()");

                    return "redirect: /user";
                }
            } catch (NotUniqueLoginException e) {
                LOG.error("Not unique login", e.getMessage());
                request.setAttribute("notUniqueLogin", true);
            }
        }

        return "/register.jsp";
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

    private void setUserSession(HttpServletRequest request, User user) {
        request.getSession().setAttribute("User", user.getLogin());
        Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");
        loggedUsers.add(user.getLogin());
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
}
