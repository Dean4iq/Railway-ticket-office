package ua.training.model.util;

import java.util.ResourceBundle;

public enum AttributeResourceManager {
    INSTANCE;

    private ResourceBundle resourceBundle;

    AttributeResourceManager() {
        this.resourceBundle = ResourceBundle.getBundle(PropertiesSource.ATTRIBUTES.source);
    }

    public String getString(String key) {
        return resourceBundle.getString(key);
    }
}
