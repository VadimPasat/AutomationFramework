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

    @After(order = 0)
    public void tearDown(Scenario scenario) {

        ScenarioContext context = ScenarioContext.getScenarioContext();
        WebDriver driver = context.getData(DRIVER_KEY, WebDriver.class);

        try {
            if (scenario.isFailed() && driver != null) {
                attachScreenshot(driver);
            }
        } catch (Exception e) {
            log.error("Error during AfterHook", e);
        } finally {
            driverFactory.quitDriver();
            ScenarioContext.removeContext();
            log.info("Driver was closed");
        }
    }

    private void attachScreenshot(WebDriver driver) {
        byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);

        log.info("Attaching screenshot to Allure...");

        Allure.getLifecycle().addAttachment(
                "Failure Screenshot",
                "image/png",
                "png",
                screenshot
        );
    }
}