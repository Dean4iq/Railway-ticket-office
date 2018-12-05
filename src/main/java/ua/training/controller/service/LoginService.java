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

        //DaoFactory daoFactory = new JDBCDaoFactory();

        try /*(UserDao userDao = daoFactory.createUserDao())*/ {
            //User user = userDao.findById(username);
            //if (user != null && user.getPassword().equals(password)) {
            if (username != null && password != null && username.equals(password)) {

                request.getSession().removeAttribute("Error");

                //if (user.isAdmin()) {
                if (!username.equals("") && username.equals("Admin")) {
                    //request.getSession(true).setAttribute("Admin", user.getLogin());
                    request.getSession(true).setAttribute("Admin", username);
                    return "redirect: /admin";
                } else if(!username.equals("") && username.equals("User")){
                    //request.getSession(true).setAttribute("User", user.getLogin());
                    request.getSession(true).setAttribute("User", username);
                    return "redirect: /user";
                }
            } else if (username!= null && password != null && !username.equals(password)){
                request.getSession().setAttribute("Error", "Invalid login and/or password");
            }
        } catch (Exception e) {
            return "redirect: /exception";
        }

        return "/login.jsp";
    }
}
