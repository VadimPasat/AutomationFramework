package com.endava.automation.atf.screenshot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import java.io.IOException;

public class ScreenShot {

    public ScreenShot(String parameter) {
    }
    // Choose parameter between: ScreenShot, FullPageShot, BorderedElementShot
    public static void makeScreenShot(String parameter, WebDriver driver, String folderName,
                                      @Nullable WebElement element) throws IOException {
        switch (parameter.toLowerCase()) {
            default:
                throw new IllegalArgumentException("Wrong parameter for makeScreenShot. Accepted parameters are:\n" +
                        "ScreenShot, FullPageShot, BorderedElementShot");
            case "screenshot":
                ScreenShotUtils.takeScreenShot(folderName, driver);
                break;
            case "fullpageshot":
                ScreenShotUtils.takeFullPageShot(folderName, driver);
                break;
            case "borderedelementshot":
                ScreenShotUtils.takeFullPageShotWithBorderedElement(folderName, driver, element);
                break;
        }
    }
}
