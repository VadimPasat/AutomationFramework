package com.endava.automation.atf.apitests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import lombok.extern.log4j.Log4j;

import static io.restassured.RestAssured.get;

@Log4j
public class ApiGetRequestSteps {


    private String endpoint;
    private Response response;

    APITests APITests = new APITests();

    @Given("a user API endpoint {string}")
    public void setUserAPIEndpoint(String url) {
        this.endpoint = url;
        log.info("URL is defined");
    }

    @When("I make a GET request to the endpoint")
    public void makeGETRequestToEndpoint() {
        this.response = get(endpoint + "/api/users");
        log.info("Get request was executed");

    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals("Unexpected status code", expectedStatusCode, actualStatusCode);
        log.info("Response status code: " + response.getStatusCode());
    }

    @Then("the response body should contain user data")
    public void verifyResponseBodyContainsUserData() {
        String responseBody = response.getBody().asString();
        Assert.assertFalse("Response body is empty", responseBody.isEmpty());
        log.info("User details are: " + responseBody);
    }
}