
Feature: Login

  Background:
    Given STANDARD_USER logs in
    And Home page is displayed


  Scenario: Add an product to card and remove it
    When Select random product
    And Product was added successfully
    Then Delete product from card
    And STANDARD_USER logs out