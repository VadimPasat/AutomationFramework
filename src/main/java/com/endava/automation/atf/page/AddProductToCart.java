package com.endava.automation.atf.page;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Getter
public class AddProductToCart extends AbstractPage {

    // Names in cart page (for logging)
    @FindBys(@FindBy(css = ".cart_item .inventory_item_name"))
    private List<WebElement> productNames;

    // All "Add to cart" buttons (SauceDemo uses ids starting with "add-to-cart-")
    @FindBys(@FindBy(css = "button[id^='add-to-cart']"))
    private List<WebElement> addToCartButtons;

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

    public AddProductToCart(WebDriver driver) {
        super(driver);
    }

    /**
     * Selects a random set of products and adds them to the cart.
     * Returns the number actually added (may be less if not enough products).
     */
    public int selectRandomProducts(int requestedCount) {
        if (requestedCount <= 0) {
            throw new IllegalArgumentException("requestedCount must be > 0");
        }

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("button[id^='add-to-cart']")));

        if (addToCartButtons == null || addToCartButtons.isEmpty()) {
            log.warn("No products found to add.");
            return 0;
        }

        int count = Math.min(requestedCount, addToCartButtons.size());
        if (count < requestedCount) {
            log.info("Requested {} items but only {} available. Using {}.",
                    requestedCount, addToCartButtons.size(), count);
        }

        List<WebElement> shuffled = new ArrayList<>(addToCartButtons);
        Collections.shuffle(shuffled);

        for (int i = 0; i < count; i++) {
            WebElement button = shuffled.get(i);
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();

            // Best-effort logging of product name
            try {
                WebElement card = button.findElement(By.xpath("./ancestor::div[contains(@class,'inventory_item')]"));
                WebElement name = card.findElement(By.cssSelector(productNames.get(0).getText().trim()));
                log.info("Added product '{}' ", name);
            } catch (Exception ignore) {
                log.info("Added product {} (name not resolved)", i + 1);
            }
        }

        log.info("Total products added to cart: {}", count);
        return count;
    }

    /**
     * Opens the cart and returns true if the cart header is visible.
     */
    public boolean openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
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
     * (Renamed to match step-def usage.)
     */
    public int getBadgeCount() {
        try {
            // badge element might not exist in DOM when empty; even if proxied, access may throw
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
    public int getNumberOfItems() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cartHeader));
        } catch (Exception ignore) {
            // if header isn't visible, we still try to read the list; caller should ensure navigation
        }
        return cartItems == null ? 0 : cartItems.size();
    }

    /**
     * Verifies inside the cart page that total quantity equals expected.
     * SauceDemo typically has "1" per item, but this sums whatever is displayed.
     */
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

    private String getProductNameFromCard(WebElement card) {
        try {
            return card.findElement(By.cssSelector(".inventory_item_name"))
                    .getText()
                    .trim();
        } catch (Exception e) {
            return "Unknown product";
        }
    }
}