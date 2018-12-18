package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class UserListManagingService implements Service {
    private static final Logger log = LogManager.getLogger(UserListManagingService.class);

    @Override
    public String execute(HttpServletRequest request) {

        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (UserDao userDao = daoFactory.createUserDao()) {
            List<User> users = userDao.findAll();

            request.setAttribute("userList", users);
        } catch (Exception e) {
            log.debug("Exception in TrainListManagingService execute()");
            log.error(Arrays.toString(e.getStackTrace()));
        }

        log.debug("TrainListManagingService execute()");
        return "/WEB-INF/admin/userList.jsp";
    }
}
