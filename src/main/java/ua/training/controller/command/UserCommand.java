package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code UserCommand} provide method to provide page link to admin page
 *
 * @author Dean4iq
 * @version 1.0
 */
public class UserCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(UserCommand.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    /**
     * Returns link to the user page
     *
     * @param request provides user data to process and link to session and context
     * @return link to the page
     */
    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute(attrManager.getString(AttributeSources.USER_USERNAME),
                request.getSession().getAttribute(attrManager.getString(AttributeSources.ROLE_USER)));
        LOG.debug("execute()");
        return "/WEB-INF/user/user.jsp";
    }
}
