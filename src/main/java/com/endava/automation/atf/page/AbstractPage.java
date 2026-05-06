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

        // NEW ScenarioContext API
        ScenarioContext ctx = ScenarioContext.get();

        // Try to reuse folder if it already exists
        String folderName = ctx.get(FOLDER_KEY, String.class);

        // If no folder found → generate
        if (folderName == null) {
            folderName = DataGenerator.folderNameGenerator();
            ctx.set(FOLDER_KEY, folderName);
            log.info("📁 Screenshot folder created for scenario: {}", folderName);
        } else {
            log.info("📁 Reusing existing screenshot folder: {}", folderName);
        }

        this.folder = folderName;

        log.debug("✅ AbstractPage initialized with folder: {}", folder);
    }

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

    @Step("Take screenshot of element: {name}")
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