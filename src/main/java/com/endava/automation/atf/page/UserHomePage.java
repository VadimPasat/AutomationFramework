package com.endava.automation.atf.page;

import com.endava.automation.atf.utils.AllureUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.ByteArrayInputStream;



@Log4j2
public class UserHomePage extends AbstractPage {

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutButton;

    @FindBy(css = ".title")
    private WebElement pageTitle;

    public UserHomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        try {
            return wait
                    .until(ExpectedConditions.visibilityOf(pageTitle))
                    .isDisplayed();

        } catch (TimeoutException e) {
            log.warn("Home page not loaded", e);
            return false;
        }

    }

    public UserLoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
        return new UserLoginPage(driver);
    }
}