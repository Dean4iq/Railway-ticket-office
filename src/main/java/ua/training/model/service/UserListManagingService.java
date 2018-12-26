package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserListManagingService {
    private static final Logger LOG = LogManager.getLogger(UserListManagingService.class);

    public static List<User> getUserList() {
        List<User> users = new ArrayList<>();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (UserDao userDao = daoFactory.createUserDao()) {
            users = userDao.findAll();


        } catch (Exception e) {
            LOG.debug("Exception");
            LOG.error("Error: {}", e);
        }

        LOG.debug("TrainListManagingService execute()");
        return users;
    }

    public static User getUser(String login) {
        User user = new User();
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.findById(login);
        } catch (Exception e) {
            LOG.debug("Exception");
            LOG.error("Error: {}", e);
        }

        return user;
    }

    public static void deleteUser(User user) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.delete(user);
        } catch (Exception e) {
            LOG.debug("Exception");
            LOG.error("Error: {}", e);
        }
    }

    public static void updateUser(User user){
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.update(user);
        } catch (Exception e) {
            LOG.debug("Exception");
            LOG.error("Error: {}", e);
        }
    }
}
