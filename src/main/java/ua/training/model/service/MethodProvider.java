package ua.training.model.service;

import javax.servlet.http.HttpServletRequest;

public interface MethodProvider {
    void call(HttpServletRequest request);
}
