package com.endava.automation.atf.apitests;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.utils.AllureUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@Getter
public class ApiGetRequestSteps {

    private static final String BASE_URL_KEY = "BASE_URL";

    private String endpoint;
    private Response response;

    @Given("a user API endpoint")
    public void setUserAPIEndpoint() {

        ScenarioContext ctx = ScenarioContext.get();

        this.endpoint = ctx.get(BASE_URL_KEY, String.class);

        assertNotNull(endpoint,
                "BASE_URL is missing in ScenarioContext. Check BeforeHook @API initialization.");

        log.info("✅ API endpoint loaded from configuration: {}", endpoint);
    }

    @When("I make a GET request to the endpoint")
    public void makeGETRequestToEndpoint() {

        assertNotNull(endpoint, "Endpoint is null. Step 'Given a user API endpoint' did not run?");

        response = given()
                .get(endpoint + "/users");

        log.info("✅ GET request executed → {}/users", endpoint);
}

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {

        assertNotNull(response, "Response is null. GET request may have failed.");

        int actualStatusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode,
                "Unexpected status code returned by API");

        log.info("✅ Status code verified → {}", actualStatusCode);
    }

    @Then("the response body should contain user data")
    public void verifyResponseBodyContainsUserData() {

        assertNotNull(response, "Response is null. Cannot verify body.");

        String responseBody = response.getBody().asString();

        assertFalse(responseBody.isEmpty(), "Response body is empty");

        log.info("✅ Response body contains user data");
        log.debug("📄 Full response body:\n{}", responseBody);
    }
}