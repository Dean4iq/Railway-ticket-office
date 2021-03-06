package ua.training.model.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;

public final class LocalizationGetter {
    private static final Logger LOG = LogManager.getLogger(LocalizationGetter.class);

    public Map<String, String> getLocalization(String lang) {
        LOG.debug("extracting locale Strings");

        Map<String, String> localeKeys = new HashMap<>();

        ResourceBundle resourceBundle =
                ResourceBundle.getBundle(PropertiesSource.LOCALIZATION_STRINGS.source,
                        new Locale(lang));
        Enumeration<String> keys = resourceBundle.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            localeKeys.put(key, formatString(resourceBundle.getString(key)));
        }

        return localeKeys;
    }

    private String formatString(String value) {
        return new String(StandardCharsets.ISO_8859_1.encode(value).array());
    }
}
