package com.yoanesber;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.quarkus.test.junit.QuarkusTest;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

@QuarkusTest
class AuthResourceTest {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "P@ssw0rd";
    private static final String LOGIN_URL = "/auth/login";

    // To test the endpoint POST /auth/login
    // This test will check if the login is successful and returns a JWT token
    // with a 200 status code
    @Test
    @Order(1)
    void testLogin() {
        String userName = USERNAME;
        String password = PASSWORD;

        given()
            .contentType("application/json")
            .body("{\"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
            .when().post(LOGIN_URL)
            .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data", not(equalTo("")));
    }

    // To test the endpoint POST /auth/login with invalid credentials
    // This test will check if the login fails with a 401 status code
    @Test
    @Order(2)
    void testLoginWithInvalidCredentials() {
        String userName = "invalidUser";
        String password = "invalidPassword";

        given()
            .contentType("application/json")
            .body("{\"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
            .when().post(LOGIN_URL)
            .then()
                .statusCode(401)
                .body("message", equalTo("Invalid username or password"));
    }

    // To test the endpoint POST /auth/login with empty credentials
    // This test will check if the login fails with a 400 status code
    @Test
    @Order(3)
    void testLoginWithEmptyCredentials() {
        String userName = "";
        String password = "";

        given()
            .contentType("application/json")
            .body("{\"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
            .when().post(LOGIN_URL)
            .then()
                .statusCode(400)
                .body("message", equalTo("Invalid request"));
    }
}