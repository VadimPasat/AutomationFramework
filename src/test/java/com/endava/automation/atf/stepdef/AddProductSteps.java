package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.AddProductToCart;
import com.endava.automation.atf.page.DeleteProductFromCart;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;

import java.io.IOException;

import static com.endava.automation.atf.screenshot.ScreenshotUtils.takeScreenshot;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class AddProductSteps {

    private final ScenarioContext scenarioContext;
    private final WebDriver driver;
    private final AddProductToCart addProductToCart;
    private final DeleteProductFromCart deleteProductFromCart;

    public AddProductSteps() {
        this.scenarioContext = ScenarioContext.getScenarioContext();
        Object drv = scenarioContext.getData("driver", WebDriver.class);
        assertNotNull(drv, "WebDriver is missing from ScenarioContext. Check hooks initialization.");
        this.driver = (WebDriver) drv;

        this.addProductToCart = new AddProductToCart(driver);
        this.deleteProductFromCart = new DeleteProductFromCart(driver);
    }

    @When("^Select (\\d+) random product(?:s)?$")
    public void selectItem(int numberOfProducts) throws IOException {
        assertTrue(numberOfProducts > 0, "Number of products must be > 0");

        int actuallyAdded = addProductToCart.selectRandomProducts(numberOfProducts);
        log.info("Requested {}, actually added {}", numberOfProducts, actuallyAdded);

        assertTrue(actuallyAdded > 0, "No products were added to the cart");
        scenarioContext.saveData("selectedCount", actuallyAdded);
    }

    @Then("Access the cart")
    public void accessCart() {
        Allure.step("ALLURE WORKING");
        boolean opened = addProductToCart.openCart();
        assertTrue(opened, "Cart did not open or header not visible");
        log.info("Shopping cart is displayed");
        //addProductToCart.takeScreenshot("Cart_page");
    }

    @And("Check if the products were added successfully")
    public void checkCartQuantity() throws IOException {
        Object stored = scenarioContext.getData("selectedCount", Integer.class);
        assertNotNull(stored, "selectedCount was not stored. Did 'Select random product(s)' run?");
        int expected = (int) stored;

        int badgeCount = addProductToCart.getNumberOfItems();
        assertEquals(expected, badgeCount, "Badge count does not match selected products");

        int cartCount = addProductToCart.getNumberOfItems();
        assertEquals(expected, cartCount, "Cart items count does not match selected products");

        assertTrue(addProductToCart.verifyCartPageQuantities(expected),
                "Cart page quantities do not match selected products");

        log.info("All items were successfully added to cart (expected={})", expected);
    }
    @Then("debug allure")
    public void debugAllure() {
        io.qameta.allure.Allure.step("ALLURE WORKING");
    }

    @Then("^Delete product(?:s)? from cart$")
    public void deleteProductsFromCart() throws IOException {
        int removed = deleteProductFromCart.deleteAllProducts();
        log.info("Removed {} items from the cart", removed);
        assertTrue(removed > 0, "No items were removed from the cart");
    }
}