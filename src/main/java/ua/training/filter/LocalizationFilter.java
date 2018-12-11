package ua.training.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.util.LocalizationGetter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class LocalizationFilter implements Filter {
    private static final Logger log = LogManager.getLogger(LocalizationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Map<String, String> localizationStrings;
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        String selectedLangParameter = httpRequest.getParameter("langSelect");
        String sessionLanguage = (String) httpRequest.getSession().getAttribute("language");

        if (selectedLangParameter != null) {
            httpRequest.setAttribute("langVariable", selectedLangParameter);
            localizationStrings = getLocalizationStrings(selectedLangParameter);
            httpRequest.getSession().setAttribute("language", selectedLangParameter);
        } else if (sessionLanguage == null) {
            httpRequest.setAttribute("langVariable", "ua");
            httpRequest.getSession().setAttribute("language", "ua");
            localizationStrings = getLocalizationStrings("ua");
        } else {
            httpRequest.setAttribute("langVariable", sessionLanguage);
            localizationStrings = (Map<String, String>) httpRequest.getSession().getAttribute("mapValues");
        }

        httpRequest.getSession().setAttribute("mapValues", localizationStrings);
        httpRequest.setAttribute("localeValues", localizationStrings);

        log.debug("doFilter()");
        filterChain.doFilter(request, response);
    }

    private Map<String, String> getLocalizationStrings(String locale) {
        return new LocalizationGetter().getLocalization(locale);
    }

    @Override
    public void destroy() {

    }
}
