package com.endava.automation.atf.page;

import com.endava.automation.atf.datagenerator.DataGenerator;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertNotNull;

@Getter
@Log4j
public class DeleteProductFromCard extends AbstractPage {

    private final String folder = DataGenerator.folderNameGenerator();
    //private final String folder = DataGenerator.folderNameGenerator();

    protected final AddProductToCard addProductToCard = new AddProductToCard(super.getDriver());
    // Find all product elements
    List<WebElement> products = super.getDriver().findElements(By.xpath("//*[contains(@id, 'add-to-cart')]"));
    // Generate a random index within the products list size
    Random random = new Random();
    int randomIndex = random.nextInt(products.size());
    // Click on a random product
    WebElement randomProduct = products.get(randomIndex);


    @FindBy(xpath = "//button[contains(@class, 'cart_button') and contains(@data-test, 'remove-')]\n")
    private WebElement deleteButton;

    public DeleteProductFromCard(WebDriver driver) {
        super(driver);
    }

    public void deleteProductFromCard() throws InterruptedException, IOException {
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        makeElementScreenShot(deleteButton);
        deleteButton.click();
        assertNotNull("Failed to delete the item from cart", randomProduct);
        log.info("The item was successfully deleted");
        // log.info("The Item was successfully deleted from card");
        Thread.sleep(2000);
    }
}
