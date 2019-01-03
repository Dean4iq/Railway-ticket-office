package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.exception.NotUniqueLoginException;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.implementation.JDBCDaoFactory;
import ua.training.model.entity.User;

import java.util.List;

public class RegisterService {
    private static final Logger LOG = LogManager.getLogger(RegisterService.class);

    public static void createNewUser(User user) {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.create(user);
        } catch (Exception e) {
            LOG.error("Error in creating new user: {}", e);
        }
    }

    public static boolean checkUniqueLogin(User user) throws NotUniqueLoginException {
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        try (UserDao userDao = daoFactory.createUserDao()) {
            List<User> userList = userDao.findAll();

            if(userList.stream().noneMatch(userItem->userItem.getLogin().equals(user.getLogin()))){
                return true;
            }
        } catch (Exception e) {
            LOG.error("Error in creating new user :{}", e);
        }

        throw new NotUniqueLoginException(user.getLogin());
    }
}
