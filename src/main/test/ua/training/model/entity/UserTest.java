package ua.training.model.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    private User testUser;

    private String login = "admin";
    private String password = "admin";
    private String name = "William";
    private String lastName = "Halsey";
    private String nameUA = "Віллям";
    private String lastNameUA = "Хелсі";
    private boolean admin = true;

    @Before
    public void setUp() throws Exception {
        testUser = new User();
        testUser.setLogin(login);
        testUser.setPassword(password);
        testUser.setName(name);
        testUser.setLastName(lastName);
        testUser.setNameUA(nameUA);
        testUser.setLastNameUA(lastNameUA);
        testUser.setAdmin(admin);
    }

    @Test
    public void checkLogin() {
        assertTrue(testUser.checkLogin());
    }

    @Test
    public void checkLoginFalse() {
        testUser.setLogin("");

        assertFalse(testUser.checkLogin());
    }

    @Test
    public void checkPassword() {
        assertTrue(testUser.checkPassword());
    }

    @Test
    public void checkPasswordFalse() {
        testUser.setPassword(null);

        assertFalse(testUser.checkPassword());
    }

    @Test
    public void getLogin() {
        assertEquals(login, testUser.getLogin());
    }

    @Test
    public void setLogin() {
        String login = "USS_CVN_rules";

        testUser.setLogin(login);

        assertEquals(login, testUser.getLogin());
    }

    @Test
    public void getPassword() {
        assertEquals(password, testUser.getPassword());
    }

    @Test
    public void setPassword() {
        String password = "ExCelSioR";

        testUser.setPassword(password);

        assertEquals(password, testUser.getPassword());
    }

    @Test
    public void getName() {
        assertEquals(name, testUser.getName());
    }

    @Test
    public void setName() {
        String name = "Stan";

        testUser.setName(name);

        assertEquals(name, testUser.getName());
    }

    @Test
    public void getLastName() {
        assertEquals(lastName, testUser.getLastName());
    }

    @Test
    public void setLastName() {
        String lastName = "Lee";

        testUser.setLastName(lastName);

        assertEquals(lastName, testUser.getLastName());
    }

    @Test
    public void getNameUA() {
        assertEquals(nameUA, testUser.getNameUA());
    }

    @Test
    public void setNameUA() {
        String nameUA = "Тарас";

        testUser.setNameUA(nameUA);

        assertEquals(nameUA, testUser.getNameUA());
    }

    @Test
    public void getLastNameUA() {
        assertEquals(lastNameUA, testUser.getLastNameUA());
    }

    @Test
    public void setLastNameUA() {
        String lastNameUA = "Тарас";

        testUser.setLastNameUA(lastNameUA);

        assertEquals(lastNameUA, testUser.getLastNameUA());
    }

    @Test
    public void isAdmin() {
        assertEquals(admin, testUser.isAdmin());
    }

    @Test
    public void setAdmin() {
        boolean admin = false;

        testUser.setAdmin(admin);

        assertEquals(admin, testUser.isAdmin());
    }

    @Test
    public void equals() {
        assertEquals(testUser, testUser);
    }
}