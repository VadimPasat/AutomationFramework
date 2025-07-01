package com.endava.automation.atf.manager;

import com.endava.automation.atf.constant.DriverType;

import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.PageFactory;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Log4j
public class DriverFactory {

    private static DriverType driverType;
    private WebDriver driver;
    ChromeOptions windowsChromeOptions = new ChromeOptions();
    FirefoxOptions windowsFirefoxOptions = new FirefoxOptions();

    public DriverFactory() {
        driverType = FileReaderManager.getInstance().getConfigFileReader().getBrowser();
        //driverPath = FileReaderManager.getInstance().getConfigFileReader().getDriverPath();
    }

    public WebDriver getDriver() {
        if (driver == null)
            driver = createLocalDriver();
        PageFactory.initElements(driver, this);
        return driver;
    }

    private WebDriver createLocalDriver() {
        switch (driverType) {
            case CHROME:
                log.info("Setting up ChromeDriver for Windows");
                // "--headless"--> Optional for non-GUI mode
                windowsChromeOptions.addArguments();
                WebDriverManager.chromedriver().win().setup();
                driver = new ChromeDriver(windowsChromeOptions);
                break;
            case FIREFOX:
                log.info("Setting up FirefoxDriver for Windows");
                windowsFirefoxOptions.addArguments();
                WebDriverManager.firefoxdriver().win().setup();
                driver = new FirefoxDriver(windowsFirefoxOptions);
                break;
        }

        if (FileReaderManager.getInstance().getConfigFileReader().getBrowserWindowSize())
            driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(FileReaderManager.getInstance().
                getConfigFileReader().getImplicitlyWait(), TimeUnit.SECONDS);
        return driver;
    }
}
