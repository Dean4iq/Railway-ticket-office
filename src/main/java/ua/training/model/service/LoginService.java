package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public class LoginService implements Service {
    private static final Logger log = LogManager.getLogger(LoginService.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("LoginService class execute()");

        String username = request.getParameter("login");
        String password = request.getParameter("pass");

        if (username != null && !username.equals("")) {
            DaoFactory daoFactory = JDBCDaoFactory.getInstance();

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
                        log.debug("Admin in LoginService class execute()");

                        return "redirect: /admin";
                    } else {
                        request.getSession(true).setAttribute("User", user.getLogin());

                        log.info(new StringBuilder()
                                .append("User '")
                                .append(user.getLogin())
                                .append("' has successfully logged in"));
                        log.debug("User in LoginService class execute()");

                        return "redirect: /user";
                    }
                } else if (username != null && user != null
                        && (user.getPassword() == null || !user.getPassword().equals(password))) {
                    log.warn(new StringBuilder()
                            .append("Someone tried to log in using username '")
                            .append(username)
                            .append("'"));
                    log.debug("Failed to login while LoginService class execute()");

                    request.getSession().setAttribute("Error", "Invalid login and/or password");
                }

            } catch (Exception e) {
                log.debug("LoginService exception");
                log.error(e.getStackTrace());
                return "redirect: /exception";
            }
        }

        log.debug("New round LoginService class execute()");
        return "/login.jsp";
    }
}
