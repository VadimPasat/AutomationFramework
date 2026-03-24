package com.endava.automation.atf.page;

import com.endava.automation.atf.constant.Users;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Getter
@Log4j2
public class UserLoginPage extends AbstractPage {

    private LoginErrorComponent errorComponent;

    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public UserLoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginErrorComponent getErrorComponent() {
        if (errorComponent == null) {
            errorComponent = new LoginErrorComponent(driver);
        }
        return errorComponent;
    }

    @Step("Enter username: {username}")
    public UserLoginPage enterUsername(String username) {
        userNameField.clear();
        userNameField.sendKeys(username);
        return this;
    }

    @Step("Enter password")
    public UserLoginPage enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
        return this;
    }

    @Step("Click Login button")
    public UserLoginPage clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return this;
    }

    @Step("Login with user: {user}")
    public UserLoginPage login(Users user) {
        enterUsername(user.getUsername());
        enterPassword(user.getPassword());
        clickLogin();
        takeFullPageScreenshot("After_Login");
        return this;
    }
}