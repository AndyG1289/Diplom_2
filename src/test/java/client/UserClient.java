package client;

import io.restassured.response.Response;
import model.User;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(CREATE_USER);
    }

    @Step("Логин пользователя")
    public Response loginUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(LOGIN_USER);
    }

    @Step("Получение access token")
    public String getAccessToken(User user) {
        String fullToken = loginUser(user)
                .then()
                .extract()
                .path("accessToken");

        // убираем "Bearer "
        return fullToken.replace("Bearer ", "");
    }
}
