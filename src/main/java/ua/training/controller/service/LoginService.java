package ua.training.controller.service;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public class LoginService implements Service {
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
                        return "redirect: /admin";
                    } else {
                        request.getSession(true).setAttribute("User", user.getLogin());
                        return "redirect: /user";
                    }
                } else if (username != null && user != null
                        && (user.getPassword() == null || !user.getPassword().equals(password))) {
                    request.getSession().setAttribute("Error", "Invalid login and/or password");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "redirect: /exception";
            }
        }

        return "/login.jsp";
    }
}
