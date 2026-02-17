package com.endava.automation.atf.manager;

import com.endava.automation.atf.constant.DriverType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
        WebDriver current = DRIVER.get();

        if (!isAlive(current)) {
            DRIVER.remove(); // remove dead ref or null
            WebDriver fresh = createLocalDriver();
            configureDriver(fresh);
            DRIVER.set(fresh);

            log.debug("Created new WebDriver instance for thread {}", Thread.currentThread().getName());
            return fresh;
        }

        return current;
    }

    public void quitDriver() {
        WebDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                driver.quit();
                log.debug("Quit WebDriver for thread {}", Thread.currentThread().getName());
            }
        } catch (Exception e) {
            log.warn("Error quitting WebDriver: {}", e.getMessage(), e);
        } finally {
            DRIVER.remove(); // always
        }
    }

    private boolean isAlive(WebDriver d) {
        if (d == null) return false;

        try {
            if (d instanceof RemoteWebDriver rwd) {
                return rwd.getSessionId() != null;
            }
            // If it’s a wrapper we can’t inspect, assume alive unless it throws.
            d.getTitle(); // lightweight call
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    private WebDriver createLocalDriver() {
        return switch (driverType) {
            case CHROME -> createChrome();
            case FIREFOX -> createFirefox();
            default -> throw new IllegalStateException("Unsupported driver type: " + driverType);
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
                "--disable-features=PasswordLeakDetection,PasswordManagerOnboarding",
                "--disable-notifications",
                "--no-default-browser-check",
                "--disable-save-password-bubble",
                "--incognito",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("autofill.credit_card_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }

    private WebDriver createFirefox() {
        log.info("Starting FirefoxDriver...");
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("signon.rememberSignons", false);
        options.addPreference("signon.generation.enabled", false);
        options.addPreference("signon.management.page.breach-alerts.enabled", false);
        options.addPreference("dom.webnotifications.enabled", false);

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
                log.warn("Unable to maximize window: {}", e.getMessage());
            }
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
        // Consider 0 implicit + explicit waits in pages for stability.
    }
}