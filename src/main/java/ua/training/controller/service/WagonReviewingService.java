package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class WagonReviewingService implements Service {
    private static final Logger log = LogManager.getLogger(WagonReviewingService.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("WagonReviewingService execute()");
        return null;
    }
}
