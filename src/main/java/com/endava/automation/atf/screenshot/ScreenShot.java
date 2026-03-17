package com.endava.automation.atf.screenshot;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Log4j2
public final class ScreenShot {

    private ScreenShot() {}

    public static Path makeScreenShot(ScreenshotType type,
                                      WebDriver driver,
                                      String folderName,
                                      String customName,
                                      WebElement element) {

        Objects.requireNonNull(type, "ScreenshotType must not be null");
        Objects.requireNonNull(driver, "WebDriver must not be null");

        try {
            Path path = switch (type) {

                case SCREENSHOT ->
                        ScreenshotUtils.takeScreenshot(folderName, driver, customName);

                case FULL_PAGE ->
                        ScreenshotUtils.takeFullPageScreenshot(folderName, driver, customName);

                case BORDERED_ELEMENT -> {
                    if (element == null) {
                        throw new IllegalArgumentException("Element is null");
                    }
                    yield ScreenshotUtils.takeElementScreenshot(folderName, driver, element, customName);
                }
            };

            if (path != null) {
                attachSafely(path, type, folderName, customName);
            }

            return path;

        } catch (Exception e) {
            log.error("Failed to take screenshot: {}", type, e);
            return null;
        }
    }

    private static void attachSafely(Path path,
                                     ScreenshotType type,
                                     String folderName,
                                     String customName) {

        try {
            AllureLifecycle lifecycle = Allure.getLifecycle();

            // 🔥 CRITICAL FIX: check if test case is active
            if (lifecycle == null) {
                log.warn("Allure lifecycle is null → skipping attachment");
                return;
            }

            byte[] bytes = Files.readAllBytes(path);

            String name = String.format("%s | %s | %s",
                    folderName,
                    type,
                    (customName != null ? customName : path.getFileName()));

            Allure.addAttachment(name,
                    "image/png",
                    new ByteArrayInputStream(bytes),
                    ".png");

        } catch (IllegalStateException e) {
            log.warn("No active Allure test → skipping attachment");
        } catch (Exception e) {
            log.error("Failed to attach screenshot", e);
        }
    }
}