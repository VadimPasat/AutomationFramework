package com.endava.automation.atf.apitests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class APITests {

    Random random = new Random();
    int randomNumber = random.nextInt(10) + 1;

    @Test
    public void getTest() {
        Response response = get("https://reqres.in/api/users?page=21111111");
        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Body : " + response.getBody().asString());
        System.out.println("Time taken : " + response.getTime());
        System.out.println("Header : " + response.getHeader("content-type"));

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }


    @Test
    public void getTestUsingRestAssured() {
        given().get("https://reqres.in/api/users?page=" + randomNumber).then().statusCode(200).log().all();
    }

    @Test
    public void postTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
                .when()
                .post("https://reqres.in/api/user")
                .then()
                .statusCode(201).log().all();
    }

    @Test
    public void putTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"zion resident\" }")
                .when()
                .put("https://reqres.in/api/users/" + randomNumber)
                .then()
                .statusCode(200).log().all();
    }

    @Test
    public void deleteTest() {
        given().delete("https://reqres.in/api/users/" + randomNumber).then().statusCode(204).log().all();
    }
}
