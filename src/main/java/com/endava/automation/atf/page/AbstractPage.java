package com.endava.automation.atf.page;

import com.endava.automation.atf.datagenerator.DataGenerator;
import com.endava.automation.atf.screenshot.ScreenShot;
import com.endava.automation.atf.screenshot.ScreenShotUtils;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

@Getter
public abstract class AbstractPage {
    protected WebDriverWait wait;
    private final WebDriver driver;
    private final String folder = DataGenerator.folderNameGenerator();


    public AbstractPage(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver,30);
    }

    public void makeScreenShot() throws IOException {
        ScreenShot.makeScreenShot("ScreenShot", driver, folder, null);
    }

    public void makeElementScreenShot(WebElement element) throws IOException {
        ScreenShot.makeScreenShot("BorderedElementShot", driver, folder, element);
    }

    public void makeFullPageShot() throws IOException {
        ScreenShot.makeScreenShot("FullPageShot", driver, folder, null);
    }

    public void borderTheElement(WebElement element) {
        ScreenShotUtils.borderElement(element, driver);
    }

}
