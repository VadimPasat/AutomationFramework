package com.endava.automation.atf.page;

import com.endava.automation.atf.utils.AllureUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Getter
public class AddProductToCart extends AbstractPage {

    // Cart icon + (optional) badge
    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge; // absent when 0 items

    // Cart header
    @FindBy(xpath = "//*[normalize-space()='Your Cart']")
    private WebElement cartHeader;

    // Quantity cells in cart page
    @FindBys(@FindBy(css = ".cart_quantity"))
    private List<WebElement> cartQuantities;

    // Cart items on cart page (each row/item)
    @FindBys(@FindBy(css = ".cart_item"))
    private List<WebElement> cartItems;

    // Names in cart page (for logging/verification on cart page)
    @FindBys(@FindBy(css = ".cart_item .inventory_item_name"))
    private List<WebElement> productNamesInCart;

    // -------- Inventory page locators (used for add-to-cart flow) --------
    private static final By INVENTORY_CARDS = By.cssSelector(".inventory_item");
    private static final By INVENTORY_NAME_IN_CARD = By.cssSelector(".inventory_item_name");
    private static final By ADD_TO_CART_IN_CARD = By.cssSelector("button[id^='add-to-cart']");

    public AddProductToCart(WebDriver driver) {
        super(driver);
    }

    @Step("Select a random product")
    public int selectRandomProducts(int requestedCount) {
        if (requestedCount <= 0) {
            throw new IllegalArgumentException("requestedCount must be > 0");
        }

        // Wait for inventory to be present
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(INVENTORY_CARDS));

        List<WebElement> cards = getDriver().findElements(INVENTORY_CARDS);
        if (cards.isEmpty()) {
            log.warn("No products found to add.");
            return 0;
        }

        int count = Math.min(requestedCount, cards.size());
        if (count < requestedCount) {
            log.info("Requested {} items but only {} available. Using {}.",
                    requestedCount, cards.size(), count);
        }

        List<WebElement> shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);

        for (int i = 0; i < count; i++) {
            WebElement card = shuffledCards.get(i);

            try {
                String name = getProductNameFromCard(card);
                WebElement addBtn = card.findElement(ADD_TO_CART_IN_CARD);

                wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
                log.info("Added product {} ('{}')", i + 1, name);
            } catch (Exception e) {
                log.info("Added product {} (name not resolved)", i + 1);
                log.debug("Failed to resolve name/click for product index {}", i + 1, e);
            }
        }

        log.info("Total products added to cart: {}", count);
        return count;
    }

    @Step("User is opening the cart")
    public boolean openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        takeFullPageScreenshot("Cart_Opened");
        try {
            wait.until(ExpectedConditions.visibilityOf(cartHeader));
            log.info("Cart opened successfully.");
            return true;
        } catch (TimeoutException e) {
            log.warn("Cart header not visible in time.", e);
            return false;
        }
    }

    /**
     * Reads the cart badge (top-right). Returns the integer count or 0 if absent.
     */
    public int getBadgeCount() {
        try {
            String txt = cartBadge.getText().trim();
            return txt.isEmpty() ? 0 : Integer.parseInt(txt);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Returns the count of items listed in the cart page.
     * Assumes you are already on the cart page.
     */
    @Step("Extract number of items")
    public int getNumberOfItems() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cartHeader));
        } catch (Exception ignore) {
            // caller should ensure navigation; we still return what we can
        }
        return cartItems == null ? 0 : cartItems.size();
    }

    /**
     * Verifies inside the cart page that total quantity equals expected.
     * SauceDemo typically has "1" per item, but this sums whatever is displayed.
     */
    @Step("Verify the cart quantities")
    public boolean verifyCartPageQuantities(int expected) {
        int sum = 0;

        if (cartQuantities != null) {
            for (WebElement q : cartQuantities) {
                String t = q.getText().trim();
                if (!t.isEmpty()) {
                    try {
                        sum += Integer.parseInt(t);
                    } catch (NumberFormatException ignore) {
                        // ignore malformed quantity cells
                    }
                }
            }
        }

        boolean ok = sum == expected;
        if (ok) {
            log.info("Cart page total quantity ({}) matches expected ({}).", sum, expected);
        } else {
            log.warn("Cart page total quantity ({}) DOES NOT match expected ({}).", sum, expected);
        }
        return ok;
    }
    @Step("Get the product name")
    private String getProductNameFromCard(WebElement card) {
        try {
            return card.findElement(INVENTORY_NAME_IN_CARD).getText().trim();
        } catch (Exception e) {
            return "Unknown product";
        }
    }
}