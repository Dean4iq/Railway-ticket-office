package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public class LoginService implements Service {
    private final Logger log = LogManager.getLogger(LoginService.class);

    @Override
    public String execute(HttpServletRequest request) {
        String username = request.getParameter("login");
        String password = request.getParameter("pass");

        if (username != null && !username.equals("")) {
            DaoFactory daoFactory = new JDBCDaoFactory();

            try (UserDao userDao = daoFactory.createUserDao()) {
                User user = userDao.findById(username);

                if (user != null && user.getPassword() != null
                        && user.getPassword().equals(password)) {

                    request.getSession().removeAttribute("Error");

                    if (user.isAdmin()) {
                        request.getSession(true).setAttribute("Admin", user.getLogin());

                        log.info(new StringBuilder()
                                .append("Admin '")
                                .append(user.getLogin())
                                .append("' has successfully logged in"));

                        return "redirect: /admin";
                    } else {
                        request.getSession(true).setAttribute("User", user.getLogin());

                        log.info(new StringBuilder()
                                .append("User '")
                                .append(user.getLogin())
                                .append("' has successfully logged in"));

                        return "redirect: /user";
                    }
                } else if (username != null && user != null
                        && (user.getPassword() == null || !user.getPassword().equals(password))) {
                    log.warn(new StringBuilder()
                            .append("Someone tried to log in using username '")
                            .append(username)
                            .append("'"));

                    request.getSession().setAttribute("Error", "Invalid login and/or password");
                }

            } catch (Exception e) {
                log.error(e.toString());
                return "redirect: /exception";
            }
        }

        return "/login.jsp";
    }
}
