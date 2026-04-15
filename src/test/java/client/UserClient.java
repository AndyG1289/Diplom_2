package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import model.LoginRequest;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String DELETE_USER = "/api/auth/user";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .accept("application/json")
                .body(user)
                .post(CREATE_USER);
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {

        LoginRequest loginRequest = new LoginRequest(
                user.getEmail(),
                user.getPassword()
        );

        return given()
                .header("Content-type", "application/json")
                .accept("application/json")
                .body(loginRequest)
                .post(LOGIN_USER);
    }

    @Step("Получение access token")
    public String getAccessToken(User user) {
        String fullToken = loginUser(user)
                .then()
                .extract()
                .path("accessToken");

        return fullToken.replace("Bearer ", "");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .accept("application/json")
                .delete(DELETE_USER);
    }
}
