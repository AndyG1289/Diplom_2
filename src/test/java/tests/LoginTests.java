package tests;

import client.UserClient;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;
import specification.BaseSpec;
import utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginTests extends BaseSpec {

    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Логин пользователя — успешный")
    public void loginSuccessfully() {
        User user = UserGenerator.getRandomUser();

        userClient.createUser(user); // сначала создаём

        Response response = userClient.loginUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertTrue(success);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Логин пользователя — неверные данные")
    public void loginWithInvalidCredentials() {
        User user = new User("wrong@yandex.ru", "wrongpassword", "Andrey");

        Response response = userClient.loginUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertFalse(success);
        assertEquals(401, statusCode);
    }
}
