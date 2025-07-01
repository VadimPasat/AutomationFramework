#@Run
Feature: Testing User API

  Background:
    Given a user API endpoint "https://reqres.in"

  Scenario: Get user from API
    When I make a GET request to the endpoint
    Then the response status code should be 200
    And the response body should contain user data

  Scenario: Combined API Requests
    When I make a simple GET request to the endpoint
    Then I Make a get via rest api request to the endpoint
    Then I make a post request to the endpoint
    Then I make a put request to the endpoint
    And I make a delete request to the endpoint
