package com.endava.automation.atf.utils;

import com.endava.automation.atf.page.AbstractPage;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class AllureUtils {

    public static void attachRequest(String name, String request) {
        Allure.addAttachment(name + " - Request", "application/json", request);
    }

    public static void attachResponse(String name, String response) {
        Allure.addAttachment(name + " - Response", "application/json", response);
    }

    public static void attachHeaders(String name, Map<String, String> headers) {
        Allure.addAttachment(name + " - Headers", headers.toString());
    }

    public static void attachLog(String name, String log) {
        Allure.addAttachment(name + " - Log", log);
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, content);
    }
    /*
    ///Example usage
    AllureUtils.attachRequest("Create User",requestBody);
    AllureUtils.attachResponse("Create User",responseBody);
    AllureUtils.attachHeaders("Create User",headers);
    ///Auto attach via RestAssured filter:
    given()
    .filter(new AllureRestAssured())
    */
}