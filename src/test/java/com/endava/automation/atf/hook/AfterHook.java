package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import io.cucumber.java.After;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

@Log4j2
public class AfterHook {

    @After("@AfterHook")
    public void after() {
        ScenarioContext context = ScenarioContext.getScenarioContext();

        try {
            WebDriver driver = context.getData("driver", WebDriver.class);

            if (driver != null) {
                driver.quit();
                log.info("Driver was closed");
            } else {
                log.warn("No WebDriver found in ScenarioContext under key 'driver' - skipping quit()");
            }
        } catch (Exception e) {
            // Don't fail teardown; keep cleanup deterministic
            log.error("Error during AfterHook cleanup (driver quit)", e);
        } finally {
            // Always clean ThreadLocal to prevent leaks between scenarios
            ScenarioContext.removeContext();
        }
    }
}