package com.endava.automation.atf.stepdef;


import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.AbstractPage;
import com.endava.automation.atf.page.AddProductToCard;
import com.endava.automation.atf.page.DeleteProductFromCard;
import com.endava.automation.atf.screenshot.CreateFolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

@Getter
@Log4j
public class AddProductSteps {


    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    private final WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final AddProductToCard addProductToCard = new AddProductToCard(webDriver);
    private final DeleteProductFromCard deleteProductFromCard = new DeleteProductFromCard(webDriver);

    @When("^Select (\\d+) random product(?:s)?$")
    public void selectItem(int numberOfProducts) throws InterruptedException, IOException {
        CreateFolder.createFolder(addProductToCard.getFolder());
        addProductToCard.selectRandomProduct(numberOfProducts);
    }

    @Then("Access the cart")
    public void accessCart() throws IOException, InterruptedException {
        addProductToCard.clickOnShoppingCard();
        log.info("Shopping cart is displayed");
        scenarioContext.saveData("checkCardQuantity", addProductToCard);
    }

    @And("Check if the products were added successfully")
    public void checkCardQuantity() throws IOException, InterruptedException {
        addProductToCard.checkCardQuantity();
        log.info("All the items were successfully added to cart");
    }

    @Then("^Delete product(?:s)? from card$")
    public void deleteProductFromCard() throws InterruptedException, IOException {
        CreateFolder.createFolder(deleteProductFromCard.getFolder());
        deleteProductFromCard.makeElementScreenShot(deleteProductFromCard.getDeleteButton());
        deleteProductFromCard.deleteProductFromCard();
        log.info("All the items were successfully deleted");
    }
}