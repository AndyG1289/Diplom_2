package specification;

import io.restassured.RestAssured;
import org.junit.Before;

public class BaseSpec {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
    }
}
