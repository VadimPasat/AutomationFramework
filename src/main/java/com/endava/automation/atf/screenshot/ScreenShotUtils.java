package com.endava.automation.atf.screenshot;


import com.endava.automation.atf.configreader.ConfigFileReader;
import com.endava.automation.atf.datagenerator.DataGenerator;
import com.endava.automation.atf.manager.FileReaderManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Log4j2
public class ScreenShotUtils {

    private static final ConfigFileReader configReader = FileReaderManager.getInstance().getConfigFileReader();
    private static String name;

    // taking ScreenShotUtils method
    public static void takeScreenShot(String folderName, WebDriver driver) throws IOException {
        if (configReader.confirmScreenShot()) {
            name = DataGenerator.screenShotNameGenerator();
            String path = configReader.getScreenShotSaveDirectoryPath() + "/" + folderName
                    + "/" + name
                    + ".png";
            screenShot(driver, path);
            log.info("Screen Shot [" + name + "] saved to folder: " + folderName);
        }
    }

    // take full page ScreenShotUtils method
    public static void takeFullPageShot(String folderName, WebDriver driver) throws IOException {
        if (configReader.confirmScreenShot()) {
            name = DataGenerator.screenShotNameGenerator();
            String path = configReader.getScreenShotSaveDirectoryPath() + "/" + folderName
                    + "/Full_" + name
                    + ".png";
            fullScreenShot(driver, path);
            log.info("Screen Shot [" + name + "] saved to folder: " + folderName);
        }
    }

    //screenshot with bordered element
    public static void takeFullPageShotWithBorderedElement(String folderName, WebDriver driver, WebElement element)
            throws IOException {
        if (configReader.confirmScreenShot()) {
            borderElement(element, driver);
            name = DataGenerator.screenShotNameGenerator();
            String path = configReader.getScreenShotSaveDirectoryPath() + "/" + folderName
                    + "/Element_" + name
                    + ".png";
            fullScreenShot(driver, path);
            log.info("Screen Shot [" + name + "] saved to folder: " + folderName);
        }
    }

    // private methods:
    // adding borders around element
    public static void borderElement(WebElement element, WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    private static float scalingWindow(WebDriver driver) {
        Object output = ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
        String value = String.valueOf(output);
        return Float.parseFloat(value);
    }

    // actual screenshot take
    private static void screenShot(WebDriver driver, String path) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(path));
    }

    // actual full screenshot take
    private static void fullScreenShot(WebDriver driver, String path) throws IOException {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies
                        .viewportPasting(ShootingStrategies.scaling(scalingWindow(driver)), 1000))
                .takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "png", new File(path));
    }
}
