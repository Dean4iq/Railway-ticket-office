package ua.training.model.util;

public enum LanguageISO {
    ENGLISH("en"),
    UKRAINIAN("uk");

    String code;

    LanguageISO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
