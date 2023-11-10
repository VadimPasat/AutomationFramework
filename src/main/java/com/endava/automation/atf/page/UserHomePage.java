package com.endava.automation.atf.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class UserHomePage extends AbstractPage {


    @FindBy(css = "[href*='account/account'].dropdown-toggle")
    private WebElement myAccountButton;

    public UserHomePage(WebDriver driver) {
        super(driver);
    }
}