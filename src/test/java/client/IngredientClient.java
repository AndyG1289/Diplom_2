package client;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientClient {

    private static final String GET_INGREDIENTS = "/api/ingredients";

    public Response getIngredients() {
        return given()
                .get(GET_INGREDIENTS);
    }
}
