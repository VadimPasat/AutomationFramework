package com.endava.automation.atf.apitests;

import com.endava.automation.atf.manager.FileReaderManager;
import com.endava.automation.atf.utils.AllureUtils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITests {

    private static final String API_KEY =
            System.getenv().getOrDefault("REQRES_API_KEY", ""); // ✅ FIXED


    private static final String BASE_URL = System.getenv("BASE_URL") != null
            ? System.getenv("BASE_URL")
            : FileReaderManager.getInstance()
              .getConfigFileReader()
              .getBaseApiUrl();


    private static final Random RANDOM = new Random();

    @BeforeAll
    static void setup() {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new RuntimeException("API_KEY is not set. Please export a valid ReqRes API key.");
        }

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
        int randomNumber = RANDOM.nextInt(10) + 1;

        given()
                .when()
                .filter(new AllureRestAssured())
                .get("/users?page=" + randomNumber)
                .then()
                .statusCode(200)
                .log().all();

    }

    @Test
    void postTest() {
        given()
                .body("""
                        {
                          "name": "morpheus",
                          "job": "leader"
                        }
                        """)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .log().all();
    }

    @Test
    void putTest() {
        int randomNumber = RANDOM.nextInt(10) + 1;

        given()
                .body("""
                        {
                          "name": "morpheus",
                          "job": "zion resident"
                        }
                        """)
                .when()
                .put("/users/" + randomNumber)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    void deleteTest() {
        int randomNumber = RANDOM.nextInt(10) + 1;

        when()
                .delete("/users/" + randomNumber)
                .then()
                .statusCode(204)
                .log().all();
    }
}