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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Log4j2
@Getter
public class AddProductToCart extends AbstractPage {

    // Product title (stable CSS is better than long XPaths)
    @FindBys(@FindBy(css = ".inventory_item_name"))
    private List<WebElement> productNames;

    // All "Add to cart" buttons (SauceDemo uses ids starting with "add-to-cart-")
    @FindBys(@FindBy(css = "button[id^='add-to-cart']"))
    private List<WebElement> addToCartButtons;

    // Cart icon (badge text = quantity)
    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge; // may not exist when 0 items

    // Open cart
    @FindBy(className = "shopping_cart_link")
    private WebElement openCartButton;

    // Cart header
    @FindBy(xpath = "//*[normalize-space()='Your Cart']")
    private WebElement cartHeader;

    // Quantity in cart page list
    @FindBys(@FindBy(css = ".cart_quantity"))
    private List<WebElement> cartQuantities;

    private int numberOfItems = 0;

    public AddProductToCart(WebDriver driver) {
        super(driver);
    }


    /**
     * Selects a random set of products and adds them to the cart.
     * Returns the number actually added (may be less if not enough products).
     */
    public int selectRandomProducts(int requestedCount) throws IOException {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("button[id^='add-to-cart']")));
        if (addToCartButtons == null || addToCartButtons.isEmpty()) {
            log.warn("No products found to add.");
            numberOfItems = 0;
            return 0;
        }

        // Limit to available buttons
        int count = Math.min(requestedCount, addToCartButtons.size());
        if (count < requestedCount) {
            log.info("Requested {} items but only {} available. Using {}.", requestedCount, addToCartButtons.size(), count);
        }

        // Shuffle a copy to avoid reusing the same item and to prevent ConcurrentModification after clicks
        List<WebElement> shuffled = new java.util.ArrayList<>(addToCartButtons);
        Collections.shuffle(shuffled);

        for (int i = 0; i < count; i++) {
            WebElement button = shuffled.get(i);
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();

            // Best-effort: log product name near the button.
            // In SauceDemo DOM, the button is inside the same item card as the name.
            try {
                WebElement card = button.findElement(By.xpath("./ancestor::div[contains(@class,'inventory_item')]"));
                WebElement name = card.findElement(By.cssSelector(".inventory_item_name"));
                log.info("Added product {}: {}", i + 1, name.getText());
            } catch (Exception ignore) {
                log.info("Added product {} (name not resolved)", i + 1);
            }
        }
        makeFullPageShot();
        numberOfItems = count;
        log.info("Total products added to cart: {}", numberOfItems);
        return numberOfItems;
    }

    /**
     * Opens the cart and returns true if the cart header is visible.
     */
    public boolean openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(openCartButton)).click();
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
    public int getCartBadgeCount() {
        try {
            wait.until(driver -> cartBadge.isDisplayed());
            String txt = cartBadge.getText().trim();
            return txt.isEmpty() ? 0 : Integer.parseInt(txt);
        } catch (Exception e) {
            return 0; // badge absent when nothing added
        }
    }

    /**
     * Verifies the UI badge equals the number we attempted to add.
     * Returns true if it matches.
     */
    public boolean verifyBadgeMatchesSelection() throws IOException {
        int badge = getCartBadgeCount();
        boolean ok = badge == numberOfItems;
        if (ok) {
            log.info("Cart badge ({}) matches selected items ({}).", badge, numberOfItems);
        } else {
            log.warn("Cart badge ({}) DOES NOT match selected items ({}).", badge, numberOfItems);
        }
        makeFullPageShot();
        return ok;
    }

    /**
     * (Optional) Verify inside the cart page that total quantity equals what we added.
     */
    public boolean verifyCartPageQuantities() {
        int sum = 0;
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
        boolean ok = sum == numberOfItems;
        if (ok) {
            log.info("Cart page total quantity ({}) matches selected items ({}).", sum, numberOfItems);
        } else {
            log.warn("Cart page total quantity ({}) DOES NOT match selected items ({}).", sum, numberOfItems);
        }
        return ok;
    }
}
