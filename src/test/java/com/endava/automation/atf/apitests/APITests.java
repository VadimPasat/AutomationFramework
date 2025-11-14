package com.endava.automation.atf.apitests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class APITests {

    // Prefer env vars; keep defaults for local runs
    private static final String API_KEY  =
            System.getenv().getOrDefault("API_KEY", "reqres-free-v1");
    private static final String BASE_URL =
            System.getenv().getOrDefault("BASE_URL", "https://reqres.in/api");

    private final Random random = new Random();
    private final int randomNumber = random.nextInt(10) + 1;

    // <-- Runs whenever the class is loaded (Cucumber or JUnit)
    static {
        RestAssured.baseURI = BASE_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("x-api-key", API_KEY)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    void getTest() {
        Response response = get("/users?page=2");

        assertEquals(200, response.statusCode());
        System.out.println(response.prettyPrint());
    }

    @Test
    void getTestUsingRestAssured() {
        given()
                .when()
                .get("/users?page=" + randomNumber)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    void postTest() {
        given()
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .log().all();
    }

    @Test
    void putTest() {
        given()
                .body("{ \"name\": \"morpheus\", \"job\": \"zion resident\" }")
                .when()
                .put("/users/" + randomNumber)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    void deleteTest() {
        when()
                .delete("/users/" + randomNumber)
                .then()
                .statusCode(204)
                .log().all();
    }
}
