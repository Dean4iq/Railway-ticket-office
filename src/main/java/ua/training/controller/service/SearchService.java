package ua.training.controller.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class SearchService implements Service{
    private static final Logger log = LogManager.getLogger(SearchService.class);

    @Override
    public String execute(HttpServletRequest request) {
        log.debug("SearchService execute()");
        return "/search.jsp";
    }
}
