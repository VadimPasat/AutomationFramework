package com.endava.automation.atf.page;


import com.endava.automation.atf.datagenerator.DataGenerator;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
@Log4j
public class CheckoutProduct extends AbstractPage {

    private final String folder = DataGenerator.folderNameGenerator();
    String filePath = "src/main/resources/CheckOutInformation.csv";

    @FindBy(xpath = "//*[@id=\"header_container\"]/div[1]/div[2]/div")
    private WebElement AppLogo;

    @FindBy(xpath = "//*[@id=\"checkout_summary_container\"]/div/div[2]/div[1]")
    private WebElement PaymentInformation;

    @FindBy(xpath = "//*[@id=\"checkout_complete_container\"]/h2")
    private WebElement CheckoutCompleted;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(xpath = "//*[@id=\"continue\"]")
    private WebElement ContinueButton;

    @FindBy(xpath = "//*[@id=\"finish\"]")
    private WebElement FinishButton;

    @FindBy(xpath = "//*[@id=\"back-to-products\"]")
    private WebElement BackHomeButton;

    @FindBy(xpath = "//*[@id=\"first-name\"]")
    private WebElement firstNameForm;

    @FindBy(xpath = "//*[@id=\"last-name\"]")
    private WebElement lastNameForm;

    @FindBy(xpath = "//*[@id=\"postal-code\"]")
    private WebElement postalCodeForm;

    public CheckoutProduct(WebDriver driver) throws IOException {
        super(driver);
    }

    public void checkoutButton() throws InterruptedException, IOException {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        checkoutButton.click();
        wait.until(ExpectedConditions.visibilityOf(firstNameForm));
        log.info("Checkout button was successfully pressed");
    }

    public void fillFormFromCSV() throws IOException, InterruptedException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
            if (records.isEmpty()) {
                log.error("CSV file is empty.");
                return;
            }
        } catch (IOException e) {
            log.error("Failed to read CSV file: " + e.getMessage());
            return;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(records.size());
        List<String> randomIndex = records.get(randomNumber);
        firstNameForm.sendKeys(randomIndex.get(0));
        log.info("Fill the First Name with the value:" + randomIndex.get(0));
        lastNameForm.sendKeys(randomIndex.get(1));
        log.info("Fill the Last Name with the value:" + randomIndex.get(1));
        postalCodeForm.sendKeys(randomIndex.get(2));
        log.info("Fill the Post Code with the value:" + randomIndex.get(2));
    }

    public void finishCheckout() throws InterruptedException, IOException {
        wait.until(ExpectedConditions.elementToBeClickable(ContinueButton));
        ContinueButton.click();
        wait.until(ExpectedConditions.visibilityOf(PaymentInformation));
        log.info("Continue to confirm the checkout information");
        FinishButton.click();
        wait.until(ExpectedConditions.visibilityOf(CheckoutCompleted));
        log.info("Checkout completed");
        wait.until(ExpectedConditions.visibilityOf(BackHomeButton));
        BackHomeButton.click();
        wait.until(ExpectedConditions.visibilityOf(AppLogo));
        log.info("Back on home page");
    }
}

