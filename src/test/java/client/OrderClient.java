package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String CREATE_ORDER = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Map<String, Object> body, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(body)
                .post(CREATE_ORDER);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrder(Map<String, Object> body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post(CREATE_ORDER);
    }
}
