package tests;

import client.IngredientClient;
import client.OrderClient;
import client.UserClient;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;
import specification.BaseSpec;
import utils.UserGenerator;
import io.qameta.allure.junit4.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OrderTests extends BaseSpec {

    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuth() {
        User user = UserGenerator.getRandomUser();
        userClient.createUser(user);

        String token = userClient.getAccessToken(user);

        IngredientClient ingredientClient = new IngredientClient();
        Response ingredientsResponse = ingredientClient.getIngredients();

        String ingredientId1 = ingredientsResponse.then().extract().path("data[0]._id");
        String ingredientId2 = ingredientsResponse.then().extract().path("data[1]._id");

        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", Arrays.asList(ingredientId1, ingredientId2));

        Response response = orderClient.createOrderWithAuth(body, token);

        response.prettyPrint();

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertTrue(success);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        IngredientClient ingredientClient = new IngredientClient();
        Response ingredientsResponse = ingredientClient.getIngredients();

        String ingredientId = ingredientsResponse.then().extract().path("data[0]._id");

        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", Arrays.asList(ingredientId));

        Response response = orderClient.createOrder(body);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertTrue(success); // важно: API позволяет создавать без авторизации
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        User user = UserGenerator.getRandomUser();
        userClient.createUser(user);

        String token = userClient.getAccessToken(user);

        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", new ArrayList<>());

        Response response = orderClient.createOrderWithAuth(body, token);

        int statusCode = response.statusCode();

        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredients() {
        User user = UserGenerator.getRandomUser();
        userClient.createUser(user);

        String token = userClient.getAccessToken(user);

        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", Arrays.asList("invalid_hash"));

        Response response = orderClient.createOrderWithAuth(body, token);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertFalse(success);
        assertEquals(400, statusCode); // API может вернуть 500
    }
}
