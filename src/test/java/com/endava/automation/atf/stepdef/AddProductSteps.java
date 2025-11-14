package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.AddProductToCart;
import com.endava.automation.atf.page.DeleteProductFromCart;
import com.endava.automation.atf.screenshot.CreateFolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Getter
@Log4j2
public class AddProductSteps {

    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    private final WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final AddProductToCart addProductToCart = new AddProductToCart(webDriver);
    private final DeleteProductFromCart deleteProductFromCart = new DeleteProductFromCart(webDriver);


    @When("^Select (\\d+) random product(?:s)?$")
    public void selectItem(int numberOfProducts) throws IOException {
        int actuallyAdded = addProductToCart.selectRandomProducts(numberOfProducts);
        log.info("Requested {}, actually added {}", numberOfProducts, actuallyAdded);
        assertTrue(actuallyAdded > 0, "No products were added to the cart");
        // keep for later checks
        scenarioContext.saveData("selectedCount", actuallyAdded);
    }

    @Then("Access the cart")
    public void accessCart() {
        assertTrue(addProductToCart.openCart(), "Cart did not open or header not visible");
        log.info("Shopping cart is displayed");
    }

    @And("Check if the products were added successfully")
    public void checkCartQuantity() throws IOException {
        // badge equals selected amount
        int expected = (int) scenarioContext.getDataOrDefault("selectedCount", addProductToCart.getNumberOfItems());
        assertTrue(addProductToCart.verifyBadgeMatchesSelection(), "Badge count does not match selection");
        // optional: verify quantities on the cart page too
        assertTrue(addProductToCart.verifyCartPageQuantities(), "Cart page quantities do not match selection");
        // extra safety: compare to expected
        assertEquals(expected, addProductToCart.getNumberOfItems(), "Tracked numberOfItems mismatch");
        log.info("All the items were successfully added to cart");
    }

    @Then("^Delete product(?:s)? from cart$")
    public void deleteProductFromCart() throws InterruptedException, IOException {
        CreateFolder.createFolder(deleteProductFromCart.getFolder());
        deleteProductFromCart.makeElementScreenShot(deleteProductFromCart.getDeleteButton());
        deleteProductFromCart.deleteProductFromCard();
        log.info("All the items were successfully deleted");
    }
}