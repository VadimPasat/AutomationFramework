package com.endava.automation.atf.apitests;

import io.cucumber.java.en.*;

import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import lombok.extern.log4j.Log4j;

import static io.restassured.RestAssured.get;

@Log4j
public class ApiCombinedRequestsSteps {

    APITests apiTests = new APITests();

    @When("I make a simple GET request to the endpoint")
    public void makeGETRequestToEndpoint() {
        apiTests.getTest();
        log.info("Get request was executed");
    }

    @Then("I Make a get via rest api request to the endpoint")
    public void makeGETRequestToEndpointUsingRestAssured() {
        apiTests.getTestUsingRestAssured();
        log.info("Get request using rest assured was executed");
    }

    @Then("I make a post request to the endpoint")
    public void makePostRequestToEndpointUsingRestAssured() {
        apiTests.postTest();
        log.info("Post request using rest assured was executed");
    }

    @Then("I make a put request to the endpoint")
    public void makePutRequestToEndpointUsingRestAssured() {
        apiTests.putTest();
        log.info("Put request using rest assured was executed");
    }

    @And("I make a delete request to the endpoint")
    public void makeDeleteRequestToEndpointUsingRestAssured() {
        apiTests.deleteTest();
        log.info("Put request using rest assured was executed");
    }
}
