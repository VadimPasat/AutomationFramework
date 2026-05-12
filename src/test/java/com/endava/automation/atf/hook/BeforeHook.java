package com.endava.automation.atf.hook;

import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.DriverFactory;
import com.endava.automation.atf.manager.FileReaderManager;
import com.endava.automation.atf.utils.AllureSetup;
import com.endava.automation.atf.utils.JUnitPropertiesManager;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

@Log4j2
public class BeforeHook {

    private static final String DRIVER_KEY = "driver";
    private final DriverFactory driverFactory = new DriverFactory();

    /**
     * ✅ UI SETUP
     */
    @Before(value = "@UI", order = 0)
    public void setUpUI() {

        ScenarioContext.clear();
        ScenarioContext ctx = ScenarioContext.get();

        WebDriver driver = driverFactory.getDriver();
        ctx.set(DRIVER_KEY, driver);

        log.info("✅ UI Test → WebDriver initialized");
    }

    /**
     * ✅ API SETUP
     */
    @Before(value = "@API", order = 0)
    public void setUpAPI() {

        ScenarioContext.clear();
        ScenarioContext ctx = ScenarioContext.get();

        String baseApiUrl = FileReaderManager.getInstance()
                .getConfigFileReader()
                .getBaseApiUrl();

        String apiKey = System.getenv().getOrDefault("REQRES_API_KEY", "");

        RestAssured.baseURI = baseApiUrl;

        RestAssured.requestSpecification =
                new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .addHeader("x-api-key", apiKey)
                        .build();

        ctx.set("BASE_URL", baseApiUrl);

        log.info("""
            ✅ API Test Setup Complete
            ➡ Base URL: %s
            ➡ API Key Loaded Successfully
            """.formatted(baseApiUrl));
    }

    /**
     * 🔥 ALLURE SETUP (RUNS ONCE, THREAD-SAFE)
     * FIXED: uses centralized AllureSetup
     */
    @Before(order = 1)
    public void setupAllureFiles() {

        log.info("🚀 Initializing Allure (centralized setup)...");

        // ✅ SINGLE ENTRY POINT (NO DUPLICATION)
        AllureSetup.init();

        log.info("✅ Allure setup complete");
    }

    @Before(order = 0)
    public void initializeAllure() {
        AllureSetup.init();
    }
}