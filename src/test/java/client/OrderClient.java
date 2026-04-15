package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String CREATE_ORDER = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(order)
                .post(CREATE_ORDER);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(CREATE_ORDER);
    }
}
