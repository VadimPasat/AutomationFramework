@Run
Feature: Login

  Background:
    Given STANDARD_USER logs in
    And Home page is displayed


  Scenario: Add an product to card and remove it
    When Select random product
    Then Product was added successfully
    And STANDARD_USER logs out


  #Scenario: Logout
   # And STANDARD_USER logs out