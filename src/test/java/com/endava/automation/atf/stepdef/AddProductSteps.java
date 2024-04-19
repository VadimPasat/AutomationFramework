package com.endava.automation.atf.stepdef;


import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.AddProductToCard;
import com.endava.automation.atf.page.DeleteProductFromCard;
import com.endava.automation.atf.screenshot.CreateFolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

@Getter
@Log4j
public class AddProductSteps {


    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    private final WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final AddProductToCard addProductToCard = new AddProductToCard(webDriver);
    private final DeleteProductFromCard deleteProductFromCard = new DeleteProductFromCard(webDriver);

    @When("^Select random (\\d+) of products$")
    public void selectItem(int numberOfProducts) throws InterruptedException, IOException {
        CreateFolder.createFolder(addProductToCard.getFolder());
        addProductToCard.selectRandomProduct(numberOfProducts);
        scenarioContext.saveData("addProductToCard", addProductToCard);
    }

    @And("Product was added successfully")
    public void addProductToCard() throws InterruptedException, IOException {
        addProductToCard.clickOnShoppingCard();
        scenarioContext.saveData("clickOnShoppingCard", addProductToCard);
        log.info("Shopping cart is displayed");
        addProductToCard.checkCardQuantity();
        scenarioContext.saveData("checkCardQuantity", addProductToCard);
        log.info("All the items were successfully added to cart");
    }

    @Then("Delete product from card")
    public void deleteProductFromCard() throws InterruptedException, IOException {
        CreateFolder.createFolder(deleteProductFromCard.getFolder());
        //deleteProductFromCard.makeElementScreenShot(deleteProductFromCard.getDeleteButton());
        deleteProductFromCard.deleteProductFromCard();
        scenarioContext.saveData("deleteProductFromCard", deleteProductFromCard);
        log.info("All the items were successfully deleted from card");
    }
}