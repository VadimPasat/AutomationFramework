package com.endava.automation.atf.stepdef;

import com.endava.automation.atf.constant.Users;
import com.endava.automation.atf.context.ScenarioContext;
import com.endava.automation.atf.page.UserHomePage;
import com.endava.automation.atf.page.UserLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

import static com.endava.automation.atf.constant.Messages.SUCCESSFUL_LOGIN;
import static org.junit.Assert.assertEquals;

@Getter
@Log4j
public class LoginSteps {

    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
    WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
    private final UserLoginPage userLoginPage = new UserLoginPage(webDriver);


    @Given("{} logs in")
    public void loginPage(Users user) throws InterruptedException {
        webDriver.get("http://www.saucedemo.com");
        userLoginPage.userLogin(user);
    }

    @Then("Home page is displayed")
    public void homePageIsDisplayed() throws InterruptedException {
        UserHomePage userHomePage = new UserHomePage(webDriver);
        assertEquals(userLoginPage.getAssertThatImLoggedIn().getText(), SUCCESSFUL_LOGIN.getMessage());
        scenarioContext.saveData("userHomePage", userHomePage);
    }

    @Then("{} logs out")
    public void logoutPage(Users user) throws InterruptedException {
        userLoginPage.userLogout(user);
        log.info(user + " user successfully logs out");
    }
}
