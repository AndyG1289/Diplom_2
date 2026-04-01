package client;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String CREATE_ORDER = "/api/orders";

    public Response createOrderWithAuth(Map<String, Object> body, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(body)
                .post(CREATE_ORDER);
    }

    public Response createOrder(Map<String, Object> body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post(CREATE_ORDER);
    }
}
