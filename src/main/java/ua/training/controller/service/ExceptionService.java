package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ExceptionService implements Service {
    private static final Logger log = LogManager.getLogger(ExceptionService.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("ExceptionService class execute()");
        return "/error.jsp";
    }
}
