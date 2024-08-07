package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.CheckoutProduct;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

@Getter
@Log4j
public class CheckoutSteps {

    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    private final WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final CheckoutProduct checkoutProduct = new CheckoutProduct(webDriver);

    public CheckoutSteps() throws IOException {
    }

    @Then("Press on Checkout button")
    public void pressOnCheckoutButton() throws InterruptedException, IOException {
        checkoutProduct.checkoutButton();
    }

    @And("Fill checkout information fields")
    public void fillCheckoutFields() throws InterruptedException, IOException {
        checkoutProduct.fillFormFromCSV();
    }

    @Then("Finish checkout")
    public void finishCheckout() throws InterruptedException, IOException {
        checkoutProduct.finishCheckout();
    }
}
