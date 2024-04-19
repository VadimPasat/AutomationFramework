package com.endava.automation.atf.apitests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.get;

public class ApiSteps {
    private String endpoint;
    private Response response;

    @Given("a user API endpoint {string}")
    public void setUserAPIEndpoint(String url) {
        this.endpoint = url;
    }

    @When("I make a GET request to the endpoint")
    public void makeGETRequestToEndpoint() {
        this.response = get(endpoint);
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals("Unexpected status code", expectedStatusCode, actualStatusCode);
    }

    @Then("the response body should contain user data")
    public void verifyResponseBodyContainsUserData() {
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Response body is empty", responseBody.length() > 0);
        // You can add more assertions here to validate the response body as needed
    }
}