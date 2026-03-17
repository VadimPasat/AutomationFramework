package com.endava.automation.atf.manager;

import com.endava.automation.atf.constant.DriverType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private final DriverType driverType;
    private final boolean headless;
    private final boolean maximizeWindow;
    private final long implicitWaitSeconds;

    public DriverFactory() {
        var cfg = FileReaderManager.getInstance().getConfigFileReader();
        this.driverType = cfg.getBrowser();
        this.headless = cfg.getHeadLessMode();
        this.maximizeWindow = cfg.getWindowMaximize();
        this.implicitWaitSeconds = cfg.getImplicitlyWait();
    }

    public WebDriver getDriver() {
        WebDriver driver = DRIVER.get();

        if (!isAlive(driver)) {
            DRIVER.remove();

            driver = createDriver();
            configureDriver(driver);

            DRIVER.set(driver);

            log.debug("Created WebDriver for thread {}", Thread.currentThread().getName());
        }

        return driver;
    }

    public void quitDriver() {
        WebDriver driver = DRIVER.get();

        try {
            if (driver != null) {
                driver.quit();
                log.debug("Driver quit for thread {}", Thread.currentThread().getName());
            }
        } catch (Exception e) {
            log.warn("Error quitting driver", e);
        } finally {
            DRIVER.remove();
        }
    }

    private boolean isAlive(WebDriver driver) {
        if (driver == null) return false;

        try {
            if (driver instanceof RemoteWebDriver rwd) {
                return rwd.getSessionId() != null;
            }
            driver.getTitle();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private WebDriver createDriver() {
        return switch (driverType) {
            case CHROME -> createChrome();
            case FIREFOX -> createFirefox();
            default -> throw new IllegalStateException("Unsupported driver: " + driverType);
        };
    }

    private WebDriver createChrome() {
        log.info("Starting ChromeDriver...");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new", "--disable-gpu");
        }

        options.addArguments(
                "--incognito",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-notifications"
        );

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }

    private WebDriver createFirefox() {
        log.info("Starting FirefoxDriver...");
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);
    }

    private void configureDriver(WebDriver driver) {

        if (maximizeWindow) {
            try {
                driver.manage().window().maximize();
            } catch (Exception e) {
                log.warn("Could not maximize window", e);
            }
        }

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
    }
}