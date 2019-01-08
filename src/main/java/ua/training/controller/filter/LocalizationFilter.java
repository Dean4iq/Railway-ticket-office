package ua.training.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.util.AttributeResourceManager;
import ua.training.model.util.AttributeSources;
import ua.training.model.util.LanguageISO;
import ua.training.model.util.LocalizationGetter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LocalizationFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(LocalizationFilter.class);
    private AttributeResourceManager attrManager = AttributeResourceManager.INSTANCE;

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.debug("init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Map<String, String> localizationStrings;
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        String selectedLangParameter = httpRequest
                .getParameter(attrManager.getString(AttributeSources.LANG_SELECT_PARAM));
        String sessionLanguage = (String) session
                .getAttribute(attrManager.getString(AttributeSources.LANGUAGE));

        if (selectedLangParameter != null && !selectedLangParameter.equals(sessionLanguage)) {
            httpRequest.setAttribute(attrManager.getString(AttributeSources.LANGUAGE_VALUE),
                    selectedLangParameter);
            localizationStrings = getLocalizationStrings(selectedLangParameter);
            session.setAttribute(attrManager.getString(AttributeSources.LANGUAGE), selectedLangParameter);
        } else if (sessionLanguage == null) {
            httpRequest.setAttribute(attrManager.getString(AttributeSources.LANGUAGE_VALUE),
                    LanguageISO.UKRAINIAN.getCode());
            session.setAttribute(attrManager.getString(AttributeSources.LANGUAGE),
                    LanguageISO.UKRAINIAN.getCode());
            localizationStrings = getLocalizationStrings(LanguageISO.UKRAINIAN.getCode());
        } else {
            httpRequest.setAttribute(attrManager.getString(AttributeSources.LANGUAGE_VALUE),
                    sessionLanguage);
            localizationStrings = (Map<String, String>) session
                    .getAttribute(attrManager.getString(AttributeSources.FILET_MAP_VALUES));
        }

        session.setAttribute(attrManager.getString(AttributeSources.FILET_MAP_VALUES),
                localizationStrings);
        httpRequest.setAttribute(attrManager.getString(AttributeSources.FILTER_LOCALE_VALUES),
                localizationStrings);
        setMandatoryAttributes(httpRequest);

        LOG.debug("doFilter()");
        filterChain.doFilter(request, response);
    }

    private Map<String, String> getLocalizationStrings(String locale) {
        return new LocalizationGetter().getLocalization(locale);
    }

    private void setMandatoryAttributes(HttpServletRequest request) {
        Map<String, String> menuItems = (LinkedHashMap<String, String>) request.getSession()
                .getAttribute(attrManager.getString(AttributeSources.USERBAR));
        request.setAttribute(attrManager.getString(AttributeSources.USERBAR), menuItems);
    }

    @Override
    public void destroy() {

    }
}
