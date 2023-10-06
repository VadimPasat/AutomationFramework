package com.endava.automation.atf.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;

@Getter
public class UserHomePage extends AbstractPage {

    public UserHomePage(WebDriver driver) {
        super(driver);
    }
}