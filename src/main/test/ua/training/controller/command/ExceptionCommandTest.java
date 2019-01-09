package ua.training.controller.command;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;

public class ExceptionCommandTest {
    private HttpServletRequest request;

    @Test
    public void execute() {
        assertEquals("/error.jsp", new ExceptionCommand().execute(request));
    }
}