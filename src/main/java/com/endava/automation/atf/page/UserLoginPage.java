package com.endava.automation.atf.page;

import com.endava.automation.atf.constant.Users;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Log4j2
@Getter
public class UserLoginPage extends AbstractPage {

    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement myAccountButton;

    // Prefer stable CSS selector over brittle XPath
    @FindBy(css = ".title")
    private WebElement productsTitle;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutButton;

    public UserLoginPage(WebDriver driver) {
        super(driver);
    }

    /** Logs in and returns true if Products page is visible. */
    public boolean userLogin(Users user) {
        userNameField.clear();
        userNameField.sendKeys(user.getUsername());
        passwordField.clear();
        passwordField.sendKeys(user.getPassword());
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        try {
            // Wait until we see "Products" in the header
            return wait.until(ExpectedConditions.textToBePresentInElement(productsTitle, "Products"));
        } catch (TimeoutException e) {
            log.info("Login failed or Products title not visible in time", e);
            return false;
        }
    }

    public void userLogout(Users user) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountButton));
        myAccountButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
    }
}
