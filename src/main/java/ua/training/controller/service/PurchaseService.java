package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.implement.JDBCDaoFactory;
import ua.training.model.dao.implement.JDBCSeatDao;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class PurchaseService implements Service {
    private static final Logger log = LogManager.getLogger(PurchaseService.class);

    @Override
    public String execute(HttpServletRequest request) {
        try (JDBCSeatDao seatDao = (JDBCSeatDao) new JDBCDaoFactory().createSeatDao();
             Connection connection = seatDao.getConnection()) {

            new Thread(() -> {
                try {
                    Thread.sleep(360000);
                    if (!connection.isClosed()) {
                        connection.rollback();
                        connection.close();
                    }
                } catch (InterruptedException | SQLException e) {
                    log.error(Arrays.toString(e.getStackTrace()));
                }
            }).start();

            //TODO button listener
            if (request.getParameter("pay") != null) {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e) {
                    log.error(Arrays.toString(e.getStackTrace()));
                }
            } else if (request.getParameter("decline") != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    log.error(Arrays.toString(e.getStackTrace()));
                }
            }


            return "redirect: /";
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return "/purchase.jsp";
    }
}
