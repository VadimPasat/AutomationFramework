package com.endava.automation.atf.page;

import com.endava.automation.atf.datagenerator.DataGenerator;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;


import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Getter
@Log4j
public class AddProductToCard extends AbstractPage {

    //private final String folder = DataGenerator.folderNameGenerator();

    protected final UserHomePage adminDashboardPage = new UserHomePage(super.getDriver());

    private int numberOfItems;

    // Find all product elements
    List<WebElement> products = super.getDriver().findElements(By.xpath("//*[contains(@id, 'add-to-cart')]"));
    // Generate a random index within the products list size
    Random random = new Random();
    int randomIndex = random.nextInt(products.size());
    // Click on a random product
    WebElement randomProduct = products.get(randomIndex);

    @FindBy(className = "btn_inventory")
    private WebElement randomIndexProduct;

    @FindBy(className = "shopping_cart_badge")
    private WebElement clickOnShoppingCard;

    @FindBy(xpath = "//*[contains(text(),'Your Cart')]")
    WebElement cartHeader;

    @FindAll({
            @FindBy(xpath = "//*[contains(@id, 'add-to-cart')]")
    })
    List<WebElement> totalNumberOfProducts;

    @FindBy(className = "cart_quantity")
    private WebElement cardQuantity;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartIcon;

    public AddProductToCard(WebDriver driver) {
        super(driver);
    }

    //List<WebElement> totalNumberOfProducts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(@id, 'add-to-cart')]")));

    public void selectRandomProduct(int numberOfItems) throws IOException {
        Random random = new Random();

        if (totalNumberOfProducts.size() < numberOfItems) {
            log.error("The maximum number of items on the online shopping page are: " + totalNumberOfProducts.size());
            numberOfItems = totalNumberOfProducts.size();
            log.info("This number will be set up as number of product that we want to add in the cart");
        }
        for (int i = 0; i < numberOfItems; i++) {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(@id, 'add-to-cart')]")));
            int randomIndex = random.nextInt(products.size());
            WebElement randomProduct = products.get(randomIndex);
            randomProduct.click();
            int product = i + 1;
            log.info("Product " + product + " is selected");
        }
        log.info("The number of products that were added to the cart are: " + numberOfItems);
        makeFullPageShot();
        this.numberOfItems = numberOfItems;
    }

    public void clickOnShoppingCard() throws InterruptedException, IOException {
        wait.until(ExpectedConditions.elementToBeClickable(clickOnShoppingCard));
        clickOnShoppingCard.click();
        assertTrue("Expected text is not present on the page", cartHeader.isDisplayed());
        log.info("The cart was successfully opened");
    }

    public void checkCardQuantity() throws InterruptedException, IOException {
        wait.until(ExpectedConditions.elementToBeClickable(cardQuantity));
        String cartItemCount = cartIcon.getText(); // Get the cart item count text
        assertEquals("Failed to add one or more products to card", Integer.parseInt(cartItemCount), numberOfItems);
        log.info("The number of items successfully validated");
        makeFullPageShot();
    }
}
