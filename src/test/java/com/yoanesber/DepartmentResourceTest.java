package com.yoanesber;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.quarkus.test.junit.QuarkusTest;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

@QuarkusTest
class DepartmentResourceTest {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "P@ssw0rd";
    private static final String LOGIN_URL = "/auth/login";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;


    // Fungsi untuk mendapatkan JWT token secara dinamis
    private String getJwtToken(String userName, String password) {
        return given()
            .contentType("application/json")
            .body("{\"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
            .when().post(LOGIN_URL)
            .then()
                .statusCode(200)
                .extract()
                .path("data");
    }

    // To test the endpoint GET /api/v1/departments
    @Test
    @Order(1)
    void testFindAllDepartments() {
        // Mendapatkan JWT token
        String jwtToken = getJwtToken(USERNAME, PASSWORD);

        // Parameter default untuk halaman dan ukuran jika tidak ingin override
        int page = DEFAULT_PAGE;
        int size = DEFAULT_SIZE;

        given()
            .header("Authorization", "Bearer " + jwtToken)
            .queryParam("page", page)
            .queryParam("size", size)
            .when().get("/api/v1/departments")
            .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data", hasSize(greaterThan(0)))
                .body("data[0].id", notNullValue())
                .body("data[0].deptName", notNullValue());
    }

    // To test the endpoint GET /api/v1/departments/{id}
    @Test
    @Order(2)
    void testFindDepartmentById() {
        // Mendapatkan JWT token
        String jwtToken = getJwtToken(USERNAME, PASSWORD);

        // Gantilah ID dengan nilai yang sesuai untuk data department yang ada
        String existingDeptId = "d001";  // ID yang ada di database
        
        given()
            .header("Authorization", "Bearer " + jwtToken)
            .when().get("/api/v1/departments/" + existingDeptId)
            .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.error", nullValue())
                .body("data.id", equalTo(existingDeptId))
                .body("data.deptName", notNullValue());
    }

    // To test the endpoint GET /api/v1/departments/{id} with non-existing ID
    @Test
    @Order(3)
    void testFindDepartmentByIdNotFound() {
        // Mendapatkan JWT token
        String jwtToken = getJwtToken(USERNAME, PASSWORD);

        // Gantilah ID dengan nilai yang tidak ada di database
        String nonExistingDeptId = "d999";
        
        given()
            .header("Authorization", "Bearer " + jwtToken)
            .when().get("/api/v1/departments/" + nonExistingDeptId)
            .then()
                .statusCode(404)
                .body("data", nullValue());
    }

    // To test the endpoint POST /api/v1/departments
    // Pastikan untuk mengganti ID dan nama department menggunakan data yang belum ada di database
    @Test
    @Order(4)
    void testCreateDepartment() {
        // Mendapatkan JWT token
        String jwtToken = getJwtToken(USERNAME, PASSWORD);

        // JSON untuk department baru
        String departmentJson = "{\"id\":\"d011\", \"deptName\":\"Legal\", \"active\":true, \"createdBy\":1}";

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + jwtToken)
            .body(departmentJson)
            .when().post("/api/v1/departments")
            .then()
                .statusCode(201)
                .body("data.id", equalTo("d011"))
                .body("data.deptName", equalTo("Legal"))
                .body("data.active", equalTo(true));
    }

    // To test the endpoint PUT /api/v1/departments/{id}
    // Pastikan untuk mengganti ID dengan data yang ada di database
    // Pastikan untuk mengganti nama department dengan nama yang berbeda
    @Test
    @Order(5)
    void testUpdateDepartment() {
        // Mendapatkan JWT token
        String jwtToken = getJwtToken(USERNAME, PASSWORD);

        // JSON untuk department yang akan diupdate
        String updatedDepartmentJson = "{\"deptName\":\"Updated Department\", \"active\":false, \"updatedBy\":2}";

        // Gantilah ID dengan nilai yang sesuai untuk data department yang ada
        String existingDeptId = "d010";
        
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + jwtToken)
            .body(updatedDepartmentJson)
            .when().put("/api/v1/departments/" + existingDeptId)
            .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.error", nullValue())
                .body("data.id", equalTo(existingDeptId))
                .body("data.deptName", equalTo("Updated Department"))
                .body("data.active", equalTo(false));
    }
}