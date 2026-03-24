package com.endava.automation.atf.page;

import com.endava.automation.atf.utils.AllureUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@Log4j2
public class DeleteProductFromCart extends AbstractPage {

    // Names in cart page (for logging)
    @FindBys(@FindBy(css = ".cart_item .inventory_item_name"))
    private List<WebElement> productNames;

    // All remove buttons in cart
    @FindBys(@FindBy(css = "button[id^='remove-']"))
    private List<WebElement> removeButtons;

    // Cart badge (exists only when > 0 items)
    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    public DeleteProductFromCart(WebDriver driver) {
        super(driver);
    }

    /**
     * Deletes all products from the cart page.
     * @return number of removed items
     */
    public int deleteAllProducts() {
        int removed = 0;

        // If there are no remove buttons, cart is already empty
        while (true) {
            if (removeButtons == null || removeButtons.isEmpty()) {
                break;
            }

            try {
                // Log first item name best-effort
                if (productNames != null && !productNames.isEmpty()) {
                    log.info("Deleting '{}' from the cart", productNames.get(0).getText().trim());
                } else {
                    log.info("Deleting an item from the cart");
                }

                WebElement btn = removeButtons.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
                removed++;

                // Wait for DOM to update (button list shrinks or becomes stale)
                wait.until(d -> {
                    try {
                        return removeButtons.isEmpty() || !btn.isDisplayed();
                    } catch (StaleElementReferenceException e) {
                        return true;
                    } catch (Exception e) {
                        return true;
                    }
                });

            } catch (StaleElementReferenceException e) {
                // DOM changed mid-iteration; retry loop
            }
        }
        waitUntilCartBadgeDisappears();
        log.info("Removed {} items from the cart", removed);
        return removed;
    }

    private void waitUntilCartBadgeDisappears() {
        try {
            // badge disappears when cart empty; if it doesn't exist, this will throw -> handled
            wait.until(ExpectedConditions.invisibilityOf(cartBadge));
        } catch (Exception ignored) {
            // already gone / not present — that's OK
        }
    }

    // If you still need this helper elsewhere
    public boolean isCartNotEmpty() {
        try {
            return cartBadge != null && cartBadge.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}