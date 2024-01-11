package com.endava.automation.atf.steps;


import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.AddProductToCard;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

@Getter
@Log4j
public class AddProductSteps {


    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final AddProductToCard addProductToCard = new AddProductToCard(webDriver);

    @When("Select random product")
    public void selectItem() throws InterruptedException {
        addProductToCard.selectRandomProduct();
        scenarioContext.saveData("addProductToCard", addProductToCard);
    }

    @Then("Product was added successfully")
    public void addProductToCard() throws InterruptedException {
        addProductToCard.clickOnShoppingCard();
        addProductToCard.checkCardQuantity();
    }
}