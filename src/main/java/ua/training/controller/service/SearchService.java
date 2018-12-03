package ua.training.controller.service;

import javax.servlet.http.HttpServletRequest;

public class SearchService implements Service{
    @Override
    public String execute(HttpServletRequest request) {
        return "/search.jsp";
    }
}
