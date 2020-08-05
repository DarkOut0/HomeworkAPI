import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class ApiTests {
    @Test
    public void testAvatar() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("data.avatar", hasItem("https://s3.amazonaws.com/uifaces/faces/twitter/follettkyle/128.jpg"));
    }

    @Test
    public void testRegistrationSuccessfully() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "eve.holt@reqres.in");
        data.put("password", "pistol");
        Response response = given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    public void testRegistrationUnsuccessfully() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "sydney@fife");
        Response response = given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    public void testYearsResource() {
        Response response = given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().all()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        for (int i = 0; i < jsonResponse.getList("data.id").size() - 1; i++) {
            Object temp = jsonResponse.getList("data.year").get(i);
            Object tempLocal = jsonResponse.getList("data.year").get(i + 1);
            if((int)temp < (int)tempLocal){
                Assert.assertTrue(true);
            }
        }
    }
}