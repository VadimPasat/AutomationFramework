package com.endava.automation.atf.firsttest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertTrue;

public class LoginTest {
    private WebDriver driver;

    @Given("I am on the SauceDemo login page")
    public void iAmOnLoginPage() throws InterruptedException {

        //System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        Thread.sleep(2000);
    }

    @When("I enter valid username and password")
    public void enterValidCredentials() throws InterruptedException {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        Thread.sleep(3000);
        WebElement passwordField = driver.findElement(By.id("password"));
        usernameField.sendKeys("standard_user");
        Thread.sleep(3000);
        passwordField.sendKeys("secret_sauce");
        Thread.sleep(3000);
    }

    @When("I click the login button")
    public void clickLoginButton() throws InterruptedException {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        Thread.sleep(2000);
    }

    @Then("I should be logged in")
    public void shouldBeLoggedIn() throws InterruptedException {
        WebElement productLabel = driver.findElement(By.id("item_4_img_link"));
        assertTrue(productLabel.isDisplayed());
        Thread.sleep(3000);
        driver.quit();
    }
}
