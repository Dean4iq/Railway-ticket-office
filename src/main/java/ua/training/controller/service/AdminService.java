package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class AdminService implements Service {
    private static final Logger log = LogManager.getLogger(AdminService.class);
    @Override
    public String execute(HttpServletRequest request) {
        log.debug("AdminService class execute()");
        return "/WEB-INF/admin/admin.jsp";
    }
}
