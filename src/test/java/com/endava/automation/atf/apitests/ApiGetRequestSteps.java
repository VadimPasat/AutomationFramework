package com.endava.automation.atf.apitests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@Getter
public class ApiGetRequestSteps {

    private String endpoint;
    private Response response;
    private static final String API_KEY = "reqres-free-v1";


    @Given("a user API endpoint {string}")
    public void setUserAPIEndpoint(String url) {
        this.endpoint = url;
        log.info("URL defined as: {}", url);
    }

    @When("I make a GET request to the endpoint")
    public void makeGETRequestToEndpoint() {
        this.response =  given()
                .header("x-api-key", API_KEY)
                .get(endpoint + "/api/users");
        log.info("GET request executed for {}", endpoint);
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode,
                "Unexpected status code");
        log.info("Verified response status code: {}", actualStatusCode);
    }

    @Then("the response body should contain user data")
    public void verifyResponseBodyContainsUserData() {
        String responseBody = response.getBody().asString();
        assertFalse(responseBody.isEmpty(), "Response body is empty");
        log.info("User details: {}", responseBody);
    }
}