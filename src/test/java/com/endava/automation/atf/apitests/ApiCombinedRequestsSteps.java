package com.endava.automation.atf.apitests;

import io.cucumber.java.en.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiCombinedRequestsSteps {

    private final APITests apiTests = new APITests();

    @When("I make a simple GET request to the endpoint")
    public void makeGETRequestToEndpoint() {
        apiTests.getTest();
        log.info("GET request executed");
    }

    @Then("I make a GET via REST API request to the endpoint")
    public void makeGETRequestToEndpointUsingRestAssured() {
        apiTests.getTestUsingRestAssured();
        log.info("GET request using RestAssured executed");
    }

    @Then("I make a POST request to the endpoint")
    public void makePostRequestToEndpointUsingRestAssured() {
        apiTests.postTest();
        log.info("POST request using RestAssured executed");
    }

    @Then("I make a PUT request to the endpoint")
    public void makePutRequestToEndpointUsingRestAssured() {
        apiTests.putTest();
        log.info("PUT request using RestAssured executed");
    }

    @And("I make a DELETE request to the endpoint")
    public void makeDeleteRequestToEndpointUsingRestAssured() {
        apiTests.deleteTest();
        log.info("DELETE request using RestAssured executed");
    }
}
