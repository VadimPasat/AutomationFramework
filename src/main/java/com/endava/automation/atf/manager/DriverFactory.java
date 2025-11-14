package com.endava.automation.atf.manager;

import com.endava.automation.atf.constant.DriverType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
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

    // One driver per thread
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    // Default settings (from your config)
    private final DriverType driverType;
    private final boolean headless;
    private final boolean maximizeWindow;
    private final long implicitWaitSeconds;

    public DriverFactory() {
        this.driverType = FileReaderManager.getInstance().getConfigFileReader().getBrowser();
        this.headless = FileReaderManager.getInstance().getConfigFileReader().getHeadLessMode();
        this.maximizeWindow = FileReaderManager.getInstance().getConfigFileReader().getWindowMaximize();
        this.implicitWaitSeconds = FileReaderManager.getInstance().getConfigFileReader().getImplicitlyWait();
    }

    /**
     * Returns a live WebDriver for the current thread.
     * Creates & configures a new one if absent OR if the stored one is already quit.
     */
    public WebDriver getDriver() {
        WebDriver current = DRIVER.get();
        if (!isAlive(current)) {
            if (current != null) {
                // Clean up any dead reference just in case
                DRIVER.remove();
            }
            WebDriver fresh = createLocalDriver();
            DRIVER.set(fresh);
            configureDriver(fresh);
            log.debug("Created new WebDriver instance for thread {}", Thread.currentThread().getName());
        }
        return DRIVER.get();
    }

    /**
     * Quits and removes the threads driver.
     * Always removes the ThreadLocal value to avoid reusing dead sessions.
     */
    public void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
                log.debug("Quit WebDriver for thread {}", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("Error quitting WebDriver: {}", e.getMessage());
            } finally {
                DRIVER.remove(); // CRITICAL
            }
        }
    }

    // -------------------- Internals --------------------

    // Detects whether the driver session is still alive.
    // All local drivers extend RemoteWebDriver; if sessionId is null, it's dead.
    private boolean isAlive(WebDriver d) {
        if (d == null) return false;
        try {
            return ((RemoteWebDriver) d).getSessionId() != null;
        } catch (ClassCastException e) {
            // Fallback: assume alive if we can't inspect; most local drivers are RemoteWebDriver
            return true;
        } catch (org.openqa.selenium.WebDriverException e) {
            // Any driver-level comms failure => treat as dead
            return false;
        }
    }

    private WebDriver createLocalDriver() {
        switch (driverType) {
            case CHROME:
                log.info("Starting ChromeDriver...");
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();

                // Headless (Selenium 4/Chrome 109+)
                if (headless) {
                    chromeOptions.addArguments("--headless=new", "--disable-gpu");
                }

                // Disable password manager / misc UX
                chromeOptions.addArguments(
                        "--disable-features=PasswordLeakDetection,PasswordManagerOnboarding",
                        "--disable-notifications",
                        "--no-default-browser-check",
                        "--disable-save-password-bubble",
                        "--incognito"
                );

                // Preferences
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("autofill.profile_enabled", false);
                prefs.put("autofill.credit_card_enabled", false);
                chromeOptions.setExperimentalOption("prefs", prefs);

                // Stability flags for some CI containers
                chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");

                return new ChromeDriver(chromeOptions);

            case FIREFOX:
                log.info("Starting FirefoxDriver...");
                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();

                // Disable password manager / notifications
                firefoxOptions.addPreference("signon.rememberSignons", false);
                firefoxOptions.addPreference("signon.generation.enabled", false);
                firefoxOptions.addPreference("signon.management.page.breach-alerts.enabled", false);
                firefoxOptions.addPreference("dom.webnotifications.enabled", false);

                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }

                return new FirefoxDriver(firefoxOptions);

            default:
                throw new IllegalStateException("Unsupported driver type: " + driverType);
        }
    }

    private void configureDriver(WebDriver driver) {
        if (maximizeWindow) {
            try {
                driver.manage().window().maximize();
            } catch (Exception e) {
                log.warn("Unable to maximize window: {}", e.getMessage());
            }
        }

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
        // Optional (uncomment if you want stronger defaults):
        // driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        // driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
    }
}
