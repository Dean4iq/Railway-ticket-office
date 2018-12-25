package ua.training.controller.command;

import ua.training.model.service.PurchaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PurchaseCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return new PurchaseService().execute(request);
    }
}
