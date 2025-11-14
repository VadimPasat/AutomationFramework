package com.endava.automation.atf.screenshot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

//// Simple page screenshot
//ScreenShot.take(ScreenShot.Mode.SCREENSHOT, driver, folder);
//
//// Full page screenshot
//ScreenShot.take(ScreenShot.Mode.FULL_PAGE, driver, folder);
//
//// Highlight a specific element
//ScreenShot.takeWithElement(driver, folder, someElement);
//// or:
//ScreenShot.take(ScreenShot.Mode.BORDERED_ELEMENT, driver, folder, someElement);

/**
 * Screenshot helper facade.
 * Prefer the enum-based API (take / takeWithElement) over the deprecated string API.
 */
public final class ScreenShot {

    private ScreenShot() {
        // utility class; no instances
    }

    /** Type-safe modes instead of magic strings. */
    public enum Mode {
        SCREENSHOT,         // was "ScreenShot"
        FULL_PAGE,          // was "FullPageShot"
        BORDERED_ELEMENT    // was "BorderedElementShot"
    }

    /** Type-safe API without element. */
    public static void take(Mode mode, WebDriver driver, String folderName) throws IOException {
        take(mode, driver, folderName, null);
    }

    /** Type-safe API with optional element (required for BORDERED_ELEMENT). */
    public static void take(Mode mode, WebDriver driver, String folderName, WebElement element) throws IOException {
        Objects.requireNonNull(mode, "mode must not be null");
        Objects.requireNonNull(driver, "driver must not be null");
        Objects.requireNonNull(folderName, "folderName must not be null");

        switch (mode) {
            case SCREENSHOT:
                ScreenShotUtils.takeScreenShot(folderName, driver);
                break;
            case FULL_PAGE:
                ScreenShotUtils.takeFullPageShot(folderName, driver);
                break;
            case BORDERED_ELEMENT:
                if (element == null) {
                    throw new IllegalArgumentException("element must not be null for Mode.BORDERED_ELEMENT");
                }
                ScreenShotUtils.takeFullPageShotWithBorderedElement(folderName, driver, element);
                break;
            default:
                throw new IllegalStateException("Unhandled mode: " + mode);
        }
    }

    /** Convenience alias if you prefer an explicit name for element case. */
    public static void takeWithElement(WebDriver driver, String folderName, WebElement element) throws IOException {
        take(Mode.BORDERED_ELEMENT, driver, folderName, element);
    }

    /**
     * Backward-compatible API. Avoid using; prefer the enum-based methods above.
     * Accepted values (case-insensitive): "ScreenShot", "FullPageShot", "BorderedElementShot".
     */
    @Deprecated
    public static void makeScreenShot(String parameter, WebDriver driver, String folderName, WebElement element)
            throws IOException {
        Mode mode = parseLegacy(parameter);
        take(mode, driver, folderName, element);
    }

    private static Mode parseLegacy(String parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException(errorMsg());
        }
        String p = parameter.toLowerCase(Locale.ROOT).trim();
        switch (p) {
            case "screenshot":
                return Mode.SCREENSHOT;
            case "fullpageshot":
                return Mode.FULL_PAGE;
            case "borderedelementshot":
                return Mode.BORDERED_ELEMENT;
            default:
                throw new IllegalArgumentException(errorMsg());
        }
    }

    private static String errorMsg() {
        return "Wrong parameter for makeScreenShot. Accepted parameters are: " +
                "ScreenShot, FullPageShot, BorderedElementShot";
    }
}
