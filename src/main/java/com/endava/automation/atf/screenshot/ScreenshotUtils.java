package com.endava.automation.atf.screenshot;

import com.endava.automation.atf.configreader.ConfigFileReader;
import com.endava.automation.atf.datagenerator.DataGenerator;
import com.endava.automation.atf.manager.FileReaderManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public final class ScreenshotUtils {

    private static final ConfigFileReader CONFIG =
            FileReaderManager.getInstance().getConfigFileReader();

    private ScreenshotUtils() {}

    public static Path takeScreenshot(String folder,
                                      WebDriver driver,
                                      String customName) {

        if (!CONFIG.confirmScreenShot()) return null;

        try {
            String name = (customName != null && !customName.isBlank())
                    ? customName
                    : DataGenerator.screenShotNameGenerator();

            Path path = buildPath(folder, name + ".png");
            Files.createDirectories(path.getParent());

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, path.toFile());

            log.info("Screenshot saved: {}", path);
            return path;

        } catch (Exception e) {
            log.error("Failed to take screenshot", e);
            return null;
        }
    }

    public static Path takeFullPageScreenshot(String folder,
                                              WebDriver driver,
                                              String customName) {

        if (!CONFIG.confirmScreenShot()) return null;

        try {
            String name = (customName != null && !customName.isBlank())
                    ? "FULL_" + customName
                    : "FULL_" + DataGenerator.screenShotNameGenerator();

            Path path = buildPath(folder, name + ".png");
            Files.createDirectories(path.getParent());

            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(1000))
                    .takeScreenshot(driver);

            ImageIO.write(screenshot.getImage(), "png", path.toFile());

            log.info("Full page screenshot saved: {}", path);
            return path;

        } catch (Exception e) {
            log.warn("Full page screenshot failed → fallback", e);
            return takeScreenshot(folder, driver, customName);
        }
    }

    public static Path takeElementScreenshot(String folder,
                                             WebDriver driver,
                                             WebElement element,
                                             String customName) {

        if (!CONFIG.confirmScreenShot()) return null;

        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        try {
            String name = (customName != null && !customName.isBlank())
                    ? "ELEMENT_" + customName
                    : "ELEMENT_" + DataGenerator.screenShotNameGenerator();

            Path path = buildPath(folder, name + ".png");
            Files.createDirectories(path.getParent());

            String originalStyle = getStyle(driver, element);

            try {
                highlightElement(driver, element);
                return takeFullPageScreenshot(folder, driver, name);
            } finally {
                restoreStyle(driver, element, originalStyle);
            }

        } catch (Exception e) {
            log.error("Failed to take element screenshot", e);
            return null;
        }
    }

    private static Path buildPath(String folder, String fileName) {
        return Path.of(CONFIG.getScreenShotSaveDirectoryPath(), folder, fileName);
    }

    private static void highlightElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.border='3px solid red'", element);
    }

    private static String getStyle(WebDriver driver, WebElement element) {
        Object style = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].getAttribute('style');", element);
        return style == null ? "" : style.toString();
    }

    private static void restoreStyle(WebDriver driver,
                                     WebElement element,
                                     String style) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                element,
                style == null ? "" : style
        );
    }
}