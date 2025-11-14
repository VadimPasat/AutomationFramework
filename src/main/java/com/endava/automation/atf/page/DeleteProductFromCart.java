package com.endava.automation.atf.page;

import com.endava.automation.atf.datagenerator.DataGenerator;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.List;

@Getter
@Log4j2
public class DeleteProductFromCart extends AbstractPage {

    private final String folder = DataGenerator.folderNameGenerator();

    @FindAll({
            @FindBy(css = ".inventory_item_name")
    })
    List<WebElement> productName;

    @FindBy(xpath = "//*[starts-with(@id, 'remove-')]")
    private WebElement deleteButton;


    @FindBy(xpath = "//*[@id=\"shopping_cart_container\"]/a/span")
    private WebElement emptyShoppingCart;

    public DeleteProductFromCart(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void deleteProductFromCard() throws InterruptedException, IOException {
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        makeElementScreenShot(deleteButton);
        while (isElementDisplayed(emptyShoppingCart)) {
            String deletedProductName = productName.get(0).getText().trim();
            log.info("Deleting " + deletedProductName + " from the cart");
            deleteButton.click();
        }
        makeFullPageShot();
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            log.info("No more items to be deleted");
            return false;
        }
    }
}
