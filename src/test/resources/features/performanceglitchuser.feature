@Run
@BeforeHook
@AfterHook
Feature: Performance glitch user add and delete products from cart

  Background:
    Given PERFORMANCE_GLITCH_USER logs in
    Then Home page is displayed

  Scenario: PERFORMANCE_GLITCH_USER adding and deleting product from card
    When Select 4 random products
    Then Access the cart
    And Check if the products were added successfully
    Then Delete products from cart
    And PERFORMANCE_GLITCH_USER logs out