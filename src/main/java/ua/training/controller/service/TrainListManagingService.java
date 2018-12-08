package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class TrainListManagingService implements Service{
    private static final Logger log = LogManager.getLogger(TrainListManagingService.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("TrainListManagingService execute()");
        return "/WEB-INF/admin/trainList.jsp";
    }
}
