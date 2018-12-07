package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserListManagingService implements Service {
    private final Logger log = LogManager.getLogger(UserListManagingService.class);

    @Override
    public String execute(HttpServletRequest request) {

        DaoFactory daoFactory = new JDBCDaoFactory();

        try (UserDao userDao = daoFactory.createUserDao()) {
            List<User> users = userDao.findAll();

            request.setAttribute("userList", users);
        } catch (Exception e) {
            log.error(e.toString());
        }

        return "/WEB-INF/admin/userList.jsp";
    }
}
