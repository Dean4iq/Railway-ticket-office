package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterService implements Service {
    private static final Logger log = LogManager.getLogger(RegisterService.class);

    @Override
    public String execute(HttpServletRequest request) {
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
            DaoFactory daoFactory = JDBCDaoFactory.getInstance();
            User user = new User();

            try (UserDao userDao = daoFactory.createUserDao()) {
                user.setName(userAttributes.get("name"));
                user.setLastName(userAttributes.get("lastName"));
                user.setNameUA(userAttributes.get("nameUA"));
                user.setLastNameUA(userAttributes.get("lastNameUA"));
                user.setLogin(userAttributes.get("login"));
                user.setPassword(userAttributes.get("password"));

                userDao.create(user);

                log.info(new StringBuilder()
                        .append("New user '")
                        .append(user.getLogin())
                        .append("'"));

                request.getSession().setAttribute("User", user.getLogin());

                log.debug("New user in RegisterService execute()");
                return "redirect: /user";
            } catch (Exception e) {
                log.debug("Exception in RegisterService execute()");
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }

        log.debug("New round RegisterService execute()");
        return "/register.jsp";
    }
}
