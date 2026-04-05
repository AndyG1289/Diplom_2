package tests;

import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Before;
import org.junit.Test;
import specification.BaseSpec;
import utils.UserGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginTests extends BaseSpec {

    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        userClient.createUser(user);
    }

    @Test
    @DisplayName("Логин пользователя — успешный")
    public void loginSuccessfully() {
        Response response = userClient.loginUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertTrue(success);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Логин пользователя — неверные данные")
    public void loginWithInvalidCredentials() {
        User wrongUser = new User("wrong@yandex.ru", "wrongpassword", "Andrey");

        Response response = userClient.loginUser(wrongUser);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertFalse(success);
        assertEquals(401, statusCode);
    }
}
