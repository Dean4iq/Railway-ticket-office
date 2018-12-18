package ua.training.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.util.LanguageISO;
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

        if (selectedLangParameter == null) {
            httpRequest.setAttribute("langVariable", LanguageISO.UKRAINIAN.getCode());
            localizationStrings = getLocalizationStrings(LanguageISO.UKRAINIAN.getCode());
        } else {
            httpRequest.setAttribute("langVariable", selectedLangParameter);
            localizationStrings = getLocalizationStrings(selectedLangParameter);
        }

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
