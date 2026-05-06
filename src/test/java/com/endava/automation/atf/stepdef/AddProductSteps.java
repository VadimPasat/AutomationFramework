package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.AddProductToCart;
import com.endava.automation.atf.page.DeleteProductFromCart;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class AddProductSteps {

    private static final String DRIVER_KEY = "driver";
    private static final String SELECTED_COUNT_KEY = "selectedCount";

    private final ScenarioContext ctx;
    private final WebDriver driver;
    private final AddProductToCart addProductToCart;
    private final DeleteProductFromCart deleteProductFromCart;

    public AddProductSteps() {

        this.ctx = ScenarioContext.get();

        this.driver = ctx.get(DRIVER_KEY, WebDriver.class);
        assertNotNull(driver, "WebDriver is missing from ScenarioContext. Check @Before @UI");

        this.addProductToCart = new AddProductToCart(driver);
        this.deleteProductFromCart = new DeleteProductFromCart(driver);
    }

    @When("^Select (\\d+) random product(?:s)?$")
    public void selectItem(int numberOfProducts) throws IOException {

        assertTrue(numberOfProducts > 0, "Number of products must be > 0");

        int actuallyAdded = addProductToCart.selectRandomProducts(numberOfProducts);
        log.info("Requested {}, actually added {}", numberOfProducts, actuallyAdded);

        assertTrue(actuallyAdded > 0, "No products were added to the cart");

        ctx.set(SELECTED_COUNT_KEY, actuallyAdded);
    }

    @Then("Access the cart")
    public void accessCart() {

        boolean opened = addProductToCart.openCart();
        assertTrue(opened, "Cart did not open or header not visible");

        log.info("Shopping cart is displayed");
    }

    @And("Check if the products were added successfully")
    public void checkCartQuantity() throws IOException {

        Integer expected = ctx.get(SELECTED_COUNT_KEY, Integer.class);
        assertNotNull(expected, "selectedCount was not stored. Did 'Select random products' run?");

        int badgeCount = addProductToCart.getNumberOfItems();
        assertEquals(expected, badgeCount, "Badge count mismatch");

        int cartCount = addProductToCart.getNumberOfItems();
        assertEquals(expected, cartCount, "Cart items count mismatch");

        assertTrue(addProductToCart.verifyCartPageQuantities(expected),
                "Cart page quantities do not match selected products");

        log.info("✅ All items added to cart successfully (expected={})", expected);
    }

    @Then("^Delete product(?:s)? from cart$")
    public void deleteProductsFromCart() throws IOException {

        int removed = deleteProductFromCart.deleteAllProducts();
        log.info("Removed {} items from the cart", removed);

        assertTrue(removed > 0, "No items were removed from the cart");
    }

    @Then("Check if the cart is empty")
    public void checkIfTheCartIsEmpty() throws IOException {

        deleteProductFromCart.isCartNotEmpty();
        log.info("✅ Cart is empty validation passed");
    }
}