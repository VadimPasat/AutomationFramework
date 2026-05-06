package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.CheckoutProduct;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.qameta.allure.Step;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class CheckoutSteps {

    private static final String DRIVER_KEY = "driver";

    private final ScenarioContext ctx;
    private final WebDriver driver;
    private final CheckoutProduct checkoutProduct;

    public CheckoutSteps() throws IOException {
        this.ctx = ScenarioContext.get();

        this.driver = ctx.get(DRIVER_KEY, WebDriver.class);
        assertNotNull(driver, "WebDriver is missing in ScenarioContext. Check @Before @UI hook.");

        this.checkoutProduct = new CheckoutProduct(driver);
    }

    @Then("Press on Checkout button")
    public void pressOnCheckoutButton() throws InterruptedException, IOException {
        checkoutProduct.checkoutButton();
        log.info("✅ Checkout button pressed");
    }

    @Step("Fill checkout information fields")
    @And("Fill checkout information fields")
    public void fillCheckoutFields() throws InterruptedException, IOException {
        checkoutProduct.fillFormFromCSV();
        log.info("✅ Checkout information fields filled");
    }

    @Step("Finish checkout")
    @Then("Finish checkout")
    public void finishCheckout() throws InterruptedException, IOException {
        checkoutProduct.finishCheckout();
        log.info("✅ Checkout finished successfully");
    }
}