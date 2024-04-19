package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import io.cucumber.java.After;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

@Log4j
public class AfterHook {

    @After
    public void after() {
        ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
        WebDriver driver = (WebDriver) scenarioContext.getData("driver");
        driver.quit();
        log.info("Driver was closed\n");
    }
}
