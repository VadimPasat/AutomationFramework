package com.endava.automation.atf.manager;

import com.endava.automation.atf.constant.DriverType;
import com.endava.automation.atf.page.DeleteProductFromCard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class WebDriverManager {

    private final static String CHROME_DRIVER = "chromedriver.exe";
    private final static String FIREFOX_DRIVER = "geckodriver.exe";
    private static DriverType driverType;
    private static String driverPath;
    private WebDriver driver;

    public WebDriverManager() {
        driverType = FileReaderManager.getInstance().getConfigFileReader().getBrowser();
        driverPath = FileReaderManager.getInstance().getConfigFileReader().getDriverPath();
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
                System.setProperty("webdriver.chrome.driver", driverPath + CHROME_DRIVER);
                driver = new ChromeDriver();
                break;
            case FIREFOX:
                System.setProperty("webdriver.gecko.driver", driverPath + FIREFOX_DRIVER);
                driver = new FirefoxDriver();
                break;
        }

        if (FileReaderManager.getInstance().getConfigFileReader().getBrowserWindowSize())
            driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(FileReaderManager.getInstance().
                getConfigFileReader().getImplicitlyWait(),TimeUnit.SECONDS);
        return driver;
    }
}
