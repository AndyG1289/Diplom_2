package specification;

import io.restassured.RestAssured;
import org.junit.Before;

public class BaseSpec {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";

        RestAssured.config = RestAssured.config()
                .httpClient(
                        io.restassured.config.HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", 5000)
                                .setParam("http.socket.timeout", 5000)
                );
    }
}
