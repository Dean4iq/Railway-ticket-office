package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Class {@code LogoutCommand} exists to invalidate user session by their will.
 * Also, the session should be crossed out from the user set in a context.
 *
 * @author Dean4iq
 * @version 1.0
 */
public class LogoutCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LogoutCommand.class);
    private AttributeResourceManager attrManag = AttributeResourceManager.INSTANCE;

    /**
     * Process user request for logout.
     * Logout should include process of session invalidating in context.
     *
     * @param request provides user date to process and link to session and context
     * @return link to homepage after successful logout
     */
    @Override
    public String execute(HttpServletRequest request) {
        LOG.debug("execute()");
        HttpSession session = request.getSession();
        ServletContext context = request.getServletContext();

        if (session.getAttribute(attrManag.getString(AttributeSources.ROLE_USER)) != null) {
            String userName = (String) session.getAttribute(attrManag.getString(AttributeSources.ROLE_USER));
            Set<String> loggedUsers = (Set<String>) context
                    .getAttribute(attrManag.getString(AttributeSources.LOGGED_USERS_CONTEXT));

            session.removeAttribute(attrManag.getString(AttributeSources.ROLE_USER));
            loggedUsers.remove(userName);
        } else if (session.getAttribute(attrManag.getString(AttributeSources.ROLE_ADMIN)) != null) {
            String userName = (String) session.getAttribute(attrManag.getString(AttributeSources.ROLE_ADMIN));
            Set<String> loggedUsers = (Set<String>) context
                    .getAttribute(attrManag.getString(AttributeSources.LOGGED_USERS_CONTEXT));

            session.removeAttribute(attrManag.getString(AttributeSources.ROLE_ADMIN));
            loggedUsers.remove(userName);
        }

        session.invalidate();

        return "redirect: /";
    }
}
