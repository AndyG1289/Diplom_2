package client;

import io.restassured.response.Response;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class IngredientClient {

    private static final String GET_INGREDIENTS = "/api/ingredients";

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .get(GET_INGREDIENTS);
    }
}
