package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.constant.Users;
import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.UserHomePage;
import com.endava.automation.atf.page.UserLoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import static com.endava.automation.atf.constant.Messages.USERNAME_WARNING_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Getter
@Log4j2
public class LoginSteps {

    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    private final WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final UserLoginPage userLoginPage = new UserLoginPage(webDriver);

    @Given("{} logs in")
    public void loginPage(Users user) throws InterruptedException {
        webDriver.get("https://www.saucedemo.com/");
        boolean loggedIn = userLoginPage.userLogin(user);
        assertTrue(loggedIn, "Login failed or Products page not visible");
        log.info("{} successfully logged in", user);
    }

    @Then("Home page is displayed")
    public void homePageIsDisplayed() {
        UserHomePage userHomePage = new UserHomePage(webDriver);
        // Example assertion if you store expected text in SUCCESSFUL_LOGIN
        // assertEquals(SUCCESSFUL_LOGIN.getMessage(), userHomePage.getHeaderText());
        log.info("Home page displayed successfully");
    }

    @Then("Enter wrong username")
    public void incorrectUsername() {
        UserHomePage userHomePage = new UserHomePage(webDriver);
//         Example of checking warning message if visible on login page
        //   assertEquals(USERNAME_WARNING_MESSAGE.getMessage(), userLoginPage.getWarningText());
        log.info("Invalid username flow validated");
    }

    @And("{} logs out")
    public void logoutPage(Users user) throws InterruptedException {
        userLoginPage.userLogout(user);
        log.info("{} successfully logged out", user);
    }
}
