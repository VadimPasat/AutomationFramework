Feature: Login to SauceDemo

  Scenario: User can login with valid credentials
    Given I am on the SauceDemo login page
    When I enter valid username and password
    And I click the login button
    Then I should be logged in