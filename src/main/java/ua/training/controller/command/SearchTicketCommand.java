package ua.training.controller.command;

import ua.training.model.service.SearchTicketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchTicketCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return new SearchTicketService().execute(request);
    }
}
