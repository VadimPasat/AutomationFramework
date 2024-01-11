package com.endava.automation.atf.page;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

@Getter
@Log4j
public class AddProductToCard extends AbstractPage {

    protected final UserHomePage adminDashboardPage = new UserHomePage(super.getDriver());
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


    @FindBy(className = "cart_quantity")
    private WebElement cardQuantity;

    public AddProductToCard(WebDriver driver) {
        super(driver);
    }

    public void selectRandomProduct() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(randomProduct));
        randomProduct.click();
        wait.until(ExpectedConditions.elementToBeClickable(clickOnShoppingCard));
        clickOnShoppingCard.click();
        wait.until(ExpectedConditions.elementToBeClickable(cardQuantity));
        //Thread.sleep(1000);
        cardQuantity.getSize();
    }

    public void clickOnShoppingCard() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(clickOnShoppingCard));
        //Thread.sleep(1000);
        clickOnShoppingCard.click();
        //Thread.sleep(1000);
    }

    public void checkCardQuantity() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(cardQuantity));
        // Check if the product was added to the cart
        WebElement cartIcon = super.getDriver().findElement(By.cssSelector(".shopping_cart_badge"));
        String cartItemCount = cartIcon.getText(); // Get the cart item count text
        Thread.sleep(1000);
        // Verify that the cart item count is greater than zero
        assertTrue("Product was added to cart", Integer.parseInt(cartItemCount) > 0);
        System.out.println(Integer.parseInt(cartItemCount));
        Thread.sleep(1000);
    }
}