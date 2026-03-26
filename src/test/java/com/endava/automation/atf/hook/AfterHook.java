package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.DriverFactory;
import io.cucumber.java.After;
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

        ScenarioContext context = ScenarioContext.getScenarioContext();
        WebDriver driver = context.getData(DRIVER_KEY, WebDriver.class);

        log.info("Scenario failed: {}", scenario.isFailed());
        log.info("Driver is null: {}", driver == null);

        try {
            if (scenario.isFailed() && driver != null) {
                attachScreenshot(driver, scenario);
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
            log.error("Error during driver quit", e);
        } finally {
            ScenarioContext.removeContext();
            log.info("Driver was closed and context cleared");
        }
    }

    private void attachScreenshot(WebDriver driver, Scenario scenario) {

        byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);

        log.info("Attaching screenshot to Allure...");

        Allure.addAttachment(
                "Failure Screenshot - " + scenario.getName(),
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );
    }
}