package com.endava.automation.atf.page;

import com.endava.automation.atf.constant.Users;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
    }

    public void userLogout(Users user) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountButton));
        myAccountButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        Thread.sleep(1000);
        logoutButton.click();
    }
}
