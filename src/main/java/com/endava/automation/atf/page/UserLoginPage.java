package com.endava.automation.atf.page;

import com.endava.automation.atf.constant.Users;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Getter
public class UserLoginPage extends AbstractPage {

    @FindBy(css = "[href*='account/account'].dropdown-toggle")
    private WebElement myAccountButton;

    @FindBy(css = "[href*='account/login']")
    private WebElement loginButton;

    @FindBy(id = "input-email")
    private WebElement emailField;

    @FindBy(id = "input-password")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@type='submit']")
    private WebElement submitButton;

    public UserLoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "[href*='account/logout']")
    private WebElement logoutButton;
    @FindBy(css = ".pull-right [href*='common/home']")
    private WebElement continueButton;

    public void userLogin(Users user) throws InterruptedException {

        wait.until(ExpectedConditions.elementToBeClickable(myAccountButton));
        myAccountButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        emailField.sendKeys(user.getUsername());
        passwordField.sendKeys(user.getPassword());
        submitButton.click();
    }

    public void userLogout(Users user) {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountButton));
        myAccountButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueButton.click();
    }
}
