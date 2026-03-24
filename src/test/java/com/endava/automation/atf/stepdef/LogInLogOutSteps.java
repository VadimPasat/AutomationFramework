package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.constant.Users;
import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.manager.FileReaderManager;
import com.endava.automation.atf.page.LoginErrorComponent;
import com.endava.automation.atf.page.UserHomePage;
import com.endava.automation.atf.page.UserLoginPage;
import com.endava.automation.atf.screenshot.CreateFolder;
import com.endava.automation.atf.utils.AllureUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class LogInLogOutSteps {

    private static final String DRIVER_KEY = "driver";
    private static final String FOLDER_KEY = "folderPath";
    private static final String HOME_PAGE_KEY = "homePage";
    private static final String LOGIN_PAGE_KEY = "loginPage"; // ✅ ADD THIS

    private String folderPath;

    private ScenarioContext context() {
        return ScenarioContext.getScenarioContext();
    }

    private WebDriver driver() {
        WebDriver driver = context().getData(DRIVER_KEY, WebDriver.class);
        assertNotNull(driver, "WebDriver is null. Check BeforeHook.");
        return driver;
    }

    private UserLoginPage loginPage() {
        return context().getData(LOGIN_PAGE_KEY, UserLoginPage.class);
    }

    @Given("{} logs in")
    public void login(Users user) throws IOException {

        UserLoginPage loginPage = new UserLoginPage(driver());
        folderPath = loginPage.getFolder();
        CreateFolder.createFolder(folderPath);
        context().saveData(FOLDER_KEY, folderPath);
        String baseUrl = FileReaderManager.getInstance()
                .getConfigFileReader()
                .getBaseUrl();

        driver().get(baseUrl);
        // ✅ Just perform action
        loginPage.login(user);
        // ✅ Save login page ALWAYS
        context().saveData(LOGIN_PAGE_KEY, loginPage);
        log.info("Login attempted with user: {}", user);
    }

    @Then("Home page is displayed")
    public void homePageIsDisplayed() {
        UserHomePage homePage = new UserHomePage(driver());
        boolean isLoaded = homePage.isPageLoaded();

        assertTrue(isLoaded,
                "Expected Home page, but login failed");
        context().saveData(HOME_PAGE_KEY, homePage);
    }

    @Then("Error message is displayed")
    public void errorMessageDisplayed() {
        log.info("=== VALIDATING ERROR MESSAGE ===");
        UserLoginPage loginPage = context().getData(LOGIN_PAGE_KEY, UserLoginPage.class);
        assertNotNull(loginPage, "LoginPage is null from context");
        LoginErrorComponent error = loginPage.getErrorComponent();
        log.info("Checking if error message is displayed...");
        assertTrue(error.isDisplayed(), "Error not displayed");
        String text = error.getText();
        log.info("Captured error message: [{}]", text);
        assertTrue(text.contains("Epic sadface"),
                "Unexpected error message: " + text);
        log.info("Error message validation PASSED");
    }

    @And("{} logs out")
    public void logout(Users user) {
        UserHomePage homePage = context().getData(HOME_PAGE_KEY, UserHomePage.class);
        UserLoginPage loginPage = homePage.logout();
        assertNotNull(loginPage, "Logout failed");
        log.info("{} successfully logged out", user);
    }
}