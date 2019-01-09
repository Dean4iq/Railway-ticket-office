package ua.training.controller.command;

import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;

public class AdminCommandTest {
    private HttpServletRequest request;

    private AdminCommand adminCommand = new AdminCommand();

    @Test
    public void execute() {
        assertEquals("/WEB-INF/admin/admin.jsp", adminCommand.execute(request));
    }
}