package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.DriverFactory;
import io.cucumber.java.Before;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

@Log4j
public class BeforeHook {

    DriverFactory driverManager = new DriverFactory();

    @Before("@BeforeHook")
    public void setUp() {
        WebDriver driver = driverManager.getDriver();
        log.info("Driver was initiated successfully");
        ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
        scenarioContext.saveData("driver", driver);
    }
}
