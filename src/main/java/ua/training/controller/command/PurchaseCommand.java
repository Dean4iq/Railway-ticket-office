package ua.training.controller.command;

import ua.training.model.service.PurchaseService;

import javax.servlet.http.HttpServletRequest;

public class PurchaseCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return new PurchaseService().execute(request);
    }
}
