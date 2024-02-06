package com.endava.automation.atf.steps;


import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.AddProductToCard;
import com.endava.automation.atf.page.DeleteProductFromCard;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.hu.De;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

@Getter
@Log4j
public class AddProductSteps {


    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final AddProductToCard addProductToCard = new AddProductToCard(webDriver);
    private final DeleteProductFromCard deleteProductFromCard = new DeleteProductFromCard(webDriver);

    @When("Select random product")
    public void selectItem() throws InterruptedException {
        addProductToCard.selectRandomProduct();
        scenarioContext.saveData("addProductToCard", addProductToCard);
    }

    @And("Product was added successfully")
    public void addProductToCard() throws InterruptedException {
        addProductToCard.clickOnShoppingCard();
        scenarioContext.saveData("clickOnShoppingCard", addProductToCard);
        addProductToCard.checkCardQuantity();
        scenarioContext.saveData("checkCardQuantity", addProductToCard);
    }

    @Then("Delete product from card")
    public void deleteProductFromCard() throws InterruptedException, IOException {
        deleteProductFromCard.deleteProductFromCard();
        scenarioContext.saveData("deleteProductFromCard", deleteProductFromCard);
    }
}