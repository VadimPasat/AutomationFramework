//package com.endava.automation.atf.steps;
//
//import com.endava.automation.atf.constant.TypeOfUsers;
//import com.endava.automation.atf.constant.Users;
//import com.endava.automation.atf.context.ScenarioContext;
//import com.endava.automation.atf.page.UserLoginPage;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import lombok.Getter;
//import lombok.extern.log4j.Log4j;
//import org.openqa.selenium.WebDriver;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.junit.Assert.assertThat;
//
//@Getter
//@Log4j
//public class LoginSteps {
//
//    private final ScenarioContext scenarioContext = ScenarioContext.getScenarioContext();
//    WebDriver webDriver = (WebDriver) scenarioContext.getData("driver");
//    private final TypeOfUsers typeOfUsers = new TypeOfUsers(webDriver);
//    private final UserLoginPage userLoginPage = new UserLoginPage(webDriver);
//
//    @Given("{} logs in as {}")
//    public void loginToPage(Users user, TypeOfUsers userType) throws InterruptedException {
//        switch (userType) {
//            case STANDARD_USER:
//                webDriver.get("http://fariusandco.eternalhost.info/admin/");
//                adminLoginPage.adminLogin(user);
//                break;
//            case LOCKED_OUT_USER:
//                webDriver.get("http://fariusandco.eternalhost.info/");
//                userLoginPage.userLogin(user);
//                break;
//        }
//    }
//
//    @Then("^home page for (administrator|user) is displayed$")
//    public void homePageIsDisplayed(String userType) {
//        switch (userType) {
//            case "administrator":
//                AdminDashboardPage adminDashboardPage = new AdminDashboardPage(webDriver);
//                assertThat("Page is displayed", webDriver.getCurrentUrl(),
//                        containsString(Messages.PAGE_URL_FOR_ADMINISTRATOR.getMessage()));
//                scenarioContext.saveData("adminDashboardPage", adminDashboardPage);
//                break;
//            case "user":
//                UserHomePage userHomePage = new UserHomePage(webDriver);
//                assertThat("Page is displayed", webDriver.getCurrentUrl(),
//                        containsString(Messages.PAGE_URL_FOR_NORMAL_USER.getMessage()));
//                scenarioContext.saveData("userHomePage", userHomePage);
//                break;
//        }
//    }
//
//    @Then("{} {} logs out")
//    public void logoutPage(Users user, TypeOfUsers userType) {
//        switch (userType) {
//            case STANDARD_USER:
//                userLoginPage.userLogout(user);
//                break;
//            case LOCKED_OUT_USER:
//                adminLoginPage.adminLogout(user);
//                break;
//        }
//    }
//}
