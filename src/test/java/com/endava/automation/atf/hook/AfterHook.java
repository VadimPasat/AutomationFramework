package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.DriverFactory;
import com.endava.automation.atf.utils.AllureHistoryManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

@Log4j2
public class AfterHook {

    private static final String DRIVER_KEY = "driver";
    private final DriverFactory driverFactory = new DriverFactory();

    @After(order = 1)
    public void attachScreenshotOnFailure(Scenario scenario) {

        ScenarioContext ctx = ScenarioContext.get();
        WebDriver driver = ctx.get(DRIVER_KEY, WebDriver.class);

        if (driver == null) return;

        try {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

                Allure.addAttachment(
                        "Failure Screenshot - " + scenario.getName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png"
                );
            }
        } catch (Exception e) {
            log.error("Error attaching screenshot", e);
        }
    }

    @After(order = 0)
    public void tearDown() {
        try {
            driverFactory.quitDriver();
        } catch (Exception e) {
            log.error("Error quitting driver", e);
        } finally {
            ScenarioContext.get().clearData();
            ScenarioContext.clear();
            log.info("ScenarioContext cleared");
        }
    }
    @AfterAll
    public static void persistHistory() {
        AllureHistoryManager.saveHistory();
    }
}