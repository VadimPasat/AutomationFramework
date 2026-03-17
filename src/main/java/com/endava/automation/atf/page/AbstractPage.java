package com.endava.automation.atf.page;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.datagenerator.DataGenerator;
import com.endava.automation.atf.screenshot.ScreenShot;
import com.endava.automation.atf.screenshot.ScreenshotType;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
@Getter
public abstract class AbstractPage {

    private static final String FOLDER_KEY = "screenshotFolder";

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final String folder;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        PageFactory.initElements(driver, this);

        ScenarioContext context = ScenarioContext.getScenarioContext();

        String existingFolder = context.getData(FOLDER_KEY, String.class);

        if (existingFolder == null) {
            existingFolder = DataGenerator.folderNameGenerator();
            context.saveData(FOLDER_KEY, existingFolder);
        }

        this.folder = existingFolder;

        log.debug("Initialized page with folder: {}", folder);
    }

    // 🔥 Fix for earlier error
    public WebDriver getDriver() {
        return driver;
    }

    @Step("Take screenshot: {name}")
    public void takeScreenshot(String name) {
        ScreenShot.makeScreenShot(
                ScreenshotType.SCREENSHOT,
                driver,
                folder,
                name,
                null
        );
    }

    @Step("Take full page screenshot: {name}")
    public void takeFullPageScreenshot(String name) {
        ScreenShot.makeScreenShot(
                ScreenshotType.FULL_PAGE,
                driver,
                folder,
                name,
                null
        );
    }

    @Step("Take element screenshot: {name}")
    public void takeElementScreenshot(WebElement element, String name) {
        ScreenShot.makeScreenShot(
                ScreenshotType.BORDERED_ELEMENT,
                driver,
                folder,
                name,
                element
        );
    }
}