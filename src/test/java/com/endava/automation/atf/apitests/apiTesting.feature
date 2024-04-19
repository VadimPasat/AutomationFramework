@noHook
Feature: Testing User API

  Scenario: Retrieve users from API
    Given a user API endpoint "https://reqres.in/api/users"
    When I make a GET request to the endpoint
    Then the response status code should be 200
    And the response body should contain user data
