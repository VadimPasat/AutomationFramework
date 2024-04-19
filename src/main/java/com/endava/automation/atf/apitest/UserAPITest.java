package com.endava.automation.atf.apitest;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.get;

public class UserAPITest {
    // Given
    String endpoint = "https://reqres.in/api/users";

    public void testRetrieveUsersFromAPI() {
        // When
        Response response = get(endpoint);
        System.out.println(response.getBody().asString());
        // Then
        int statusCode = response.getStatusCode();
        Assert.assertEquals("Unexpected status code", 200, statusCode);
        // And
        Assert.assertTrue("Response body is empty", response.getBody().asString().length() > 0);
    }
}