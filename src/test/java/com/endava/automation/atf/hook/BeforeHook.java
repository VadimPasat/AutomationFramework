package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.DriverFactory;
import com.endava.automation.atf.utils.AllureCategoriesWriter;
import com.endava.automation.atf.utils.AllureEnvironmentWriter;
import com.endava.automation.atf.utils.AllureExecutorWriter;
import com.endava.automation.atf.utils.AllureHistoryWriter;
import io.cucumber.java.Before;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

@Log4j2
public class BeforeHook {

    private static final String DRIVER_KEY = "driver";
    private final DriverFactory driverFactory = new DriverFactory();

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

    @Before(order = 0)
    public void setupAllureFiles() {
        AllureEnvironmentWriter.writeEnvironment();
        AllureCategoriesWriter.writeCategories();
        AllureExecutorWriter.writeExecutor();
        AllureHistoryWriter.copyHistory();
    }
}