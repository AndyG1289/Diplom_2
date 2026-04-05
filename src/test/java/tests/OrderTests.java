package tests;

import client.IngredientClient;
import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import specification.BaseSpec;
import utils.UserGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderTests extends BaseSpec {

    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getRandomUser();
        userClient.createUser(user);
        token = userClient.getAccessToken(user);
    }

    @After
    public void tearDown() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuth() {
        IngredientClient ingredientClient = new IngredientClient();
        Response ingredientsResponse = ingredientClient.getIngredients();

        String firstIngredientId = ingredientsResponse.then().extract().path("data[0]._id");
        String secondIngredientId = ingredientsResponse.then().extract().path("data[1]._id");

        Order order = new Order(Arrays.asList(firstIngredientId, secondIngredientId));

        Response response = orderClient.createOrderWithAuth(order, token);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(success);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        IngredientClient ingredientClient = new IngredientClient();
        Response ingredientsResponse = ingredientClient.getIngredients();

        String ingredientId = ingredientsResponse.then().extract().path("data[0]._id");

        Order order = new Order(Arrays.asList(ingredientId));

        Response response = orderClient.createOrder(order);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(success);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        Order order = new Order(new ArrayList<>());

        Response response = orderClient.createOrderWithAuth(order, token);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");
        String message = response.then().extract().path("message");

        assertEquals(400, statusCode);
        assertFalse(success);
        assertTrue(message.contains("Ingredient"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredients() {
        Order order = new Order(Arrays.asList("invalid_hash"));

        Response response = orderClient.createOrderWithAuth(order, token);

        int statusCode = response.statusCode();
        boolean success = response.then().extract().path("success");

        assertEquals(500, statusCode);
        assertFalse(success);
    }
}
