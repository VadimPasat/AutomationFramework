@Run
@BeforeHook
@AfterHook
Feature: Products manipulation

  Background:
    Given STANDARD_USER logs in
    Then Home page is displayed

  Scenario: Add an product to card and remove it
    When Select 20 random products
    Then Access the cart
    And Check if the products were added successfully
    Then Delete products from cart
    And STANDARD_USER logs out

  Scenario: Add an product to card and checkout
    When Select 33 random products
    Then Access the cart
    And Check if the products were added successfully
    Then Press on Checkout button
    And Fill checkout information fields
    Then Finish checkout
    And STANDARD_USER logs out