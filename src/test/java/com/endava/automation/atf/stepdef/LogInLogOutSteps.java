package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.constant.Users;
import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.FileReaderManager;
import com.endava.automation.atf.page.LoginErrorComponent;
import com.endava.automation.atf.page.UserHomePage;
import com.endava.automation.atf.page.UserLoginPage;
import com.endava.automation.atf.screenshot.CreateFolder;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class LogInLogOutSteps {

    private static final String DRIVER_KEY = "driver";
    private static final String FOLDER_KEY = "folderPath";
    private static final String HOME_PAGE_KEY = "homePage";
    private static final String LOGIN_PAGE_KEY = "loginPage";

    private final ScenarioContext ctx;
    private final WebDriver driver;

    public LogInLogOutSteps() {
        this.ctx = ScenarioContext.get();
        this.driver = ctx.get(DRIVER_KEY, WebDriver.class);

        assertNotNull(driver, "WebDriver is null. Check BeforeHook @UI initialization.");
    }

    @Given("{} logs in")
    public void login(Users user) throws IOException {

        UserLoginPage loginPage = new UserLoginPage(driver);

        String folderPath = loginPage.getFolder();
        CreateFolder.createFolder(folderPath);
        ctx.set(FOLDER_KEY, folderPath);

        String baseUrl = FileReaderManager.getInstance()
                .getConfigFileReader()
                .getBaseUrl();

        driver.get(baseUrl);

        loginPage.login(user);

        ctx.set(LOGIN_PAGE_KEY, loginPage);
        log.info("Login attempted with user: {}", user);
    }

    @Then("Home page is displayed")
    public void homePageIsDisplayed() {

        UserHomePage homePage = new UserHomePage(driver);

        assertTrue(homePage.isPageLoaded(), "Expected Home page, but login failed");

        ctx.set(HOME_PAGE_KEY, homePage);
        log.info("✅ Home page is displayed");
    }

    @Then("Error message is displayed")
    public void errorMessageDisplayed() {
        UserLoginPage loginPage = ctx.get(LOGIN_PAGE_KEY, UserLoginPage.class);
        assertNotNull(loginPage, "LoginPage not found in context.");
        LoginErrorComponent error = loginPage.getErrorComponent();
        assertTrue(error.isDisplayed(), "Error not displayed");
        String text = error.getText();
        log.info("Captured error message: [{}]", text);
        assertTrue(text.contains("Epic sadface"),
                "Unexpected error message: " + text);
        log.info("✅ Error message validation passed");
    }

    @And("{} logs out")
    public void logout(Users user) {

        UserHomePage homePage = ctx.get(HOME_PAGE_KEY, UserHomePage.class);
        assertNotNull(homePage, "HomePage not found in context");
        UserLoginPage loginPage = homePage.logout();
        assertNotNull(loginPage, "Logout failed");

        log.info("{} successfully logged out", user);
    }
}