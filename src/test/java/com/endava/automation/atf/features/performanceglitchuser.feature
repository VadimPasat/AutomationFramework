
Feature: Login


  Background:
    Given PERFORMANCE_GLITCH_USER logs in
    And Home page is displayed


  Scenario: PERFORMANCE_GLITCH_USER adding and deleting product from card
    When Select random 2 of products
    And Product was added successfully
    Then Delete product from card
    And PERFORMANCE_GLITCH_USER logs out