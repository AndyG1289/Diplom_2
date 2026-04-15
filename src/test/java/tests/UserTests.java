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

        userClient.createUser(user);

        Response response = userClient.createUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");
        String message = response.then().extract().path("message");

        assertEquals(403, statusCode);
        assertFalse(success);
        assertEquals("User already exists", message);
    }

    @Test
    @DisplayName("Создание пользователя — отсутствует email")
    public void createUserWithoutEmail() {
        User user = new User(null, "password123", "Andrey");

        Response response = userClient.createUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");
        String message = response.then().extract().path("message");

        assertEquals(403, statusCode);
        assertFalse(success);
        assertEquals("Email, password and name are required fields", message);
    }

    @Test
    @DisplayName("Создание пользователя — отсутствует имя")
    public void createUserWithoutName() {
        User user = new User("test@yandex.ru", "password123", null);

        Response response = userClient.createUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");
        String message = response.then().extract().path("message");

        assertEquals(403, statusCode);
        assertFalse(success);
        assertEquals("Email, password and name are required fields", message);
    }

    @Test
    @DisplayName("Создание пользователя — отсутствует пароль")
    public void createUserWithoutPassword() {
        User user = new User("test@yandex.ru", null, "Andrey");

        Response response = userClient.createUser(user);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");
        String message = response.then().extract().path("message");

        assertEquals(403, statusCode);
        assertFalse(success);
        assertEquals("Email, password and name are required fields", message);
    }
}
