package ua.training.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface {@code Command} exists to define method list for child classes
 *
 * @author Dean4iq
 * @version 1.0
 */
public interface Command {
    /**
     * Process user request and returns corresponding link to the page
     *
     * @param request provides user data to process and link to session and context
     * @return page link
     */
    String execute(HttpServletRequest request);
}
