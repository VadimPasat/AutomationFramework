package com.endava.automation.atf.page;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory; // ✅ ADD THIS
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginErrorComponent {

    private WebDriver driver;
    private WebDriverWait wait;

    public LoginErrorComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this); // 🔥 PUT IT HERE
    }

    @FindBy(css = ".error-message-container h3[data-test='error']")
    private WebElement errorMessage;

    public boolean isDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getText() {
        return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
    }
}