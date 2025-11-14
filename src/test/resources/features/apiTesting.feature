@Run
Feature: Testing User API

  Background:
    Given a user API endpoint "https://reqres.in"

  Scenario: Get user from API
    When I make a GET request to the endpoint
    Then the response status code should be 200
    And the response body should contain user data

  Scenario: Combined API Requests
    When I make a simple GET request to the endpoint
    Then I make a GET via REST API request to the endpoint
    Then I make a POST request to the endpoint
    Then I make a PUT request to the endpoint
    And I make a DELETE request to the endpoint
