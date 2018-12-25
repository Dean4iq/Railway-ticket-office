package ua.training.util;

public enum RegExSources {
    NAME("name", "regex.name"),
    LAST_NAME("lastName","regex.lastName"),
    NAME_UA("nameUA","regex.nameUA"),
    LAST_NAME_UA("lastNameUA","regex.lastNameUA"),
    LOGIN("login","regex.login"),
    PASSWORD("password","regex.password");

    String field;
    String link;

    RegExSources(String field, String link) {
        this.field = field;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getField() {
        return field;
    }
}
