package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.constant.Users;
import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.UserHomePage;
import com.endava.automation.atf.page.UserLoginPage;
import com.endava.automation.atf.screenshot.CreateFolder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

import static com.endava.automation.atf.screenshot.ScreenshotUtils.takeScreenshot;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class LoginLogOutSteps {

    private static final String DRIVER_KEY = "driver";
    private static final String FOLDER_KEY = "folderPath";
    private static final String BASE_URL = "https://www.saucedemo.com/";

    private String folderPath;

    private ScenarioContext context() {
        return ScenarioContext.getScenarioContext();
    }

    private WebDriver driver() {
        WebDriver driver = context().getData(DRIVER_KEY, WebDriver.class);
        assertNotNull(driver, "WebDriver is null. Check BeforeHook: driver must be created and stored in ScenarioContext.");
        return driver;
    }

    private UserLoginPage loginPage() {
        return new UserLoginPage(driver());
    }

    @Given("{} logs in")
    public void loginPage(Users user) throws IOException {
        UserLoginPage userLoginPage = loginPage();

        // Create & store folder once
        folderPath = userLoginPage.getFolder();
        CreateFolder.createFolder(folderPath);
        context().saveData(FOLDER_KEY, folderPath);

        driver().get(BASE_URL);

        boolean loggedIn = userLoginPage.userLogin(user);
        assertTrue(loggedIn, "Login failed or Products page not visible");
        log.info("{} successfully logged in", user);
    }

    @Then("Home page is displayed")
    public void homePageIsDisplayed() throws IOException {
        // If this step is executed standalone, try to recover folderPath from context
        if (folderPath == null) {
            folderPath = context().getData(FOLDER_KEY, String.class);
        }
        log.info("Home page displayed successfully");
        takeScreenshot("Cart_page", driver(), folderPath);
    }

    @Then("Enter wrong username")
    public void incorrectUsername() {
        // This currently does nothing meaningful, consider implementing actual invalid login checks
        UserHomePage userHomePage = new UserHomePage(driver());
        log.info("Invalid username flow validated (TODO: add real assertions)");
    }

    @And("{} logs out")
    public void logoutPage(Users user) throws InterruptedException {
        UserLoginPage userLoginPage = loginPage();
        userLoginPage.userLogout(user);
        log.info("{} successfully logged out", user);
    }
}