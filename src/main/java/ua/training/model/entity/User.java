package ua.training.model.entity;

import java.util.Objects;

public class User {
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String nameUA;
    private String lastNameUA;
    private boolean admin = false;

    public boolean checkLogin() {
        return (this.login != null) && !(this.login.equals(""));
    }

    public boolean checkPassword() {
        return (this.password != null) && !(this.password.equals(""));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameUA() {
        return nameUA;
    }

    public void setNameUA(String nameUA) {
        this.nameUA = nameUA;
    }

    public String getLastNameUA() {
        return lastNameUA;
    }

    public void setLastNameUA(String lastNameUA) {
        this.lastNameUA = lastNameUA;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        User user = (User) object;
        return isAdmin() == user.isAdmin()
                && getLogin().equals(user.getLogin())
                && getPassword().equals(user.getPassword())
                && getName().equals(user.getName())
                && getLastName().equals(user.getLastName())
                && getNameUA().equals(user.getNameUA())
                && getLastNameUA().equals(user.getLastNameUA());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getPassword(), getName(), getLastName(),
                getNameUA(), getLastNameUA(), isAdmin());
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nameUA='" + nameUA + '\'' +
                ", lastNameUA='" + lastNameUA + '\'' +
                ", admin=" + admin +
                '}';
    }
}
