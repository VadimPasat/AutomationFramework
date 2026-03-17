package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.DriverFactory;
import io.cucumber.java.Before;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

@Log4j2
public class BeforeHook {

    private static final String DRIVER_KEY = "driver";
    private final DriverFactory driverFactory = new DriverFactory();

    /**
     * 🔥 UI TESTS ONLY
     */
    @Before(value = "@UI", order = 0)
    public void setUpUI() {

        ScenarioContext.removeContext();
        ScenarioContext context = ScenarioContext.getScenarioContext();

        WebDriver driver = driverFactory.getDriver();

        if (driver == null) {
            throw new IllegalStateException("DriverFactory returned null WebDriver");
        }

        context.saveData(DRIVER_KEY, driver);

        log.info("UI Test → Driver initialized");
    }

    /**
     * 🔥 API TESTS ONLY
     */
    @Before(value = "@API", order = 0)
    public void setUpAPI() {

        ScenarioContext.removeContext();
        ScenarioContext context = ScenarioContext.getScenarioContext();

        // NO WebDriver here
        log.info("API Test → No browser started");

        // aici poți adăuga:
        // - base URI
        // - auth tokens
        // - headers
    }
}