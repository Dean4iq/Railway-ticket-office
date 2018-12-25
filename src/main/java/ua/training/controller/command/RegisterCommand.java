package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.User;
import ua.training.model.service.RegisterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            new RegisterService().createNewUser(user);

            LOG.info(new StringBuilder()
                    .append("New user '")
                    .append(user.getLogin())
                    .append("'"));

            setUserSession(request, user);

            LOG.debug("New user in RegisterService execute()");
            return "redirect: /user";
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
}
