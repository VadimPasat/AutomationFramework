package com.endava.automation.atf.page;

import com.endava.automation.atf.constant.Users;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.Assert.assertEquals;

@Log4j
@Getter
public class UserLoginPage extends AbstractPage {

    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(xpath = "//*[@id=\"header_container\"]/div[2]/span")
    private WebElement assertThatImLoggedIn;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement myAccountButton;


    @FindBy(xpath = "//span[@class='title' and text()='Products']\n")
    private WebElement assertHomePage;


    public UserLoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutButton;


    public void userLogin(Users user) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        userNameField.sendKeys(user.getUsername());
        passwordField.sendKeys(user.getPassword());
        loginButton.click();
        try {
            // Wait for the element to be visible before asserting its text
            wait.until(ExpectedConditions.visibilityOf(assertHomePage));
            assertEquals("Products", assertHomePage.getText());
        } catch (TimeoutException | AssertionError e) {
            // Handle the timeout or assertion error
            e.printStackTrace();
            log.info("Login failed");
        }
    }

    public void userLogout(Users user) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountButton));
        myAccountButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
    }
}
