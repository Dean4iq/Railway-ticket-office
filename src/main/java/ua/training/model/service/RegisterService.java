package ua.training.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.daoimplementation.JDBCDaoFactory;
import ua.training.model.entity.User;

public class RegisterService {
    private static final Logger LOG = LogManager.getLogger(RegisterService.class);

    public void createNewUser(User user){
        DaoFactory daoFactory = JDBCDaoFactory.getInstance();
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.create(user);
        } catch (Exception e) {
            LOG.error("Error in creating new user :{}", e);
        }
    }
}
