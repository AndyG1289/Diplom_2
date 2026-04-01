package tests;

import client.UserClient;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;
import specification.BaseSpec;
import utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserTests extends BaseSpec {

    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Создание пользователя — успешный сценарий")
    public void createUserSuccessfully() {
        User user = UserGenerator.getRandomUser();

        Response response = userClient.createUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertTrue(success);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Создание пользователя — пользователь уже существует")
    public void createUserAlreadyExists() {
        User user = UserGenerator.getRandomUser();

        userClient.createUser(user); // первый раз создаём

        Response response = userClient.createUser(user); // второй раз

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertFalse(success);
        assertEquals(403, statusCode);
    }

    @Test
    @DisplayName("Создание пользователя — отсутствует email")
    public void createUserWithoutEmail() {
        User user = new User(null, "password123", "Andrey");

        Response response = userClient.createUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertFalse(success);
        assertEquals(403, statusCode);
    }
}
