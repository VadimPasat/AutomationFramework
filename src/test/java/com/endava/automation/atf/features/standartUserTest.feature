@VadimP
@Run
Feature: Login

  Background:
    Given STANDARD_USER logs in
    And Home page is displayed


  Scenario: Add an product to card and remove it
    When Select random 10 of products
    And Product was added successfully
    Then Delete product from card
    And STANDARD_USER logs out


  Scenario: Add an product to card and checkout
    When Select random 5 of products
    And Product was added successfully
    Then Press on Checkout button
    And Fill checkout information fields
    Then Finish checkout
    And STANDARD_USER logs out