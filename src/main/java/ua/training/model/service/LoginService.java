package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.exception.InvalidLoginOrPasswordException;
import ua.training.exception.NotExistedLoginException;
import ua.training.exception.NotUniqueLoginException;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class LoginService {
    private static final Logger LOG = LogManager.getLogger(LoginService.class);

    public static boolean checkLoginExistence(User user) throws NotExistedLoginException {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (UserDao userDao = daoFactory.createUserDao()) {
            User userFromDB = userDao.findById(user.getLogin());

            if (userFromDB.checkLogin()) {
                return true;
            }
        } catch (Exception e) {
            LOG.error("Error occurred: {}", Arrays.toString(e.getStackTrace()));
        }

        throw new NotExistedLoginException(user.getLogin());
    }

    public static User checkLoginPassword(User user) throws InvalidLoginOrPasswordException {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();

        try (UserDao userDao = daoFactory.createUserDao()) {
            User userFromDB = userDao.findById(user.getLogin());

            if (userFromDB.getLogin().equals(user.getLogin())
                    && userFromDB.getPassword().equals(user.getPassword())) {
                return userFromDB;
            }
        } catch (Exception e) {
            LOG.error("Error occurred: {}", Arrays.toString(e.getStackTrace()));
        }

        throw new InvalidLoginOrPasswordException(user.getLogin());
    }
}
