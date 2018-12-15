package ua.training.controller.command;

import ua.training.model.service.SearchTicketService;

import javax.servlet.http.HttpServletRequest;

public class SearchTicketCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new SearchTicketService().execute(request);
    }
}
