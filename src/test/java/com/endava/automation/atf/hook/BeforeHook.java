package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.DriverFactory;
import io.cucumber.java.Before;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

@Log4j2
public class BeforeHook {

    private static final String DRIVER_KEY = "driver";
    private final DriverFactory driverManager = new DriverFactory();

    @Before("@BeforeHook")
    public void setUp() {
        ScenarioContext.removeContext();

        ScenarioContext context = ScenarioContext.getScenarioContext();
        WebDriver driver = driverManager.getDriver();

        if (driver == null) {
            throw new IllegalStateException("DriverFactory returned null WebDriver");
        }
        context.saveData(DRIVER_KEY, driver);
        log.info("Driver was initiated successfully");
    }
}