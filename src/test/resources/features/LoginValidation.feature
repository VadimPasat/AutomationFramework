#@UI
Feature: Login validation

  Scenario: Invalid username shows error message
    Given WRONG_USERNAME logs in
    Then Error message is displayed

  Scenario: Invalid password shows error message
    Given WRONG_PASSWORD logs in
    Then Error message is displayed