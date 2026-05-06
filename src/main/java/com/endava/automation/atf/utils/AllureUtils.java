package com.endava.automation.atf.utils;

import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
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
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Log4j2
public final class AllureUtils {

    private AllureUtils() { }

    // ========================================================================
    // ✅ API ATTACHMENTS
    // ========================================================================

    public static void attachRequest(String name, String requestBody) {
        if (requestBody == null) return;
        Allure.addAttachment(name + " - Request", "application/json",
                new ByteArrayInputStream(pretty(requestBody).getBytes()), "json");
    }

    public static void attachResponse(String name, String responseBody) {
        if (responseBody == null) return;
        Allure.addAttachment(name + " - Response", "application/json",
                new ByteArrayInputStream(pretty(responseBody).getBytes()), "json");
    }

    public static void attachHeaders(String name, Map<String, ?> headers) {
        if (headers == null) return;
        Allure.addAttachment(
                name + " - Headers",
                headers.toString()
        );
    }

    public static void attachLog(String name, String content) {
        if (content == null) return;
        Allure.addAttachment(name + " - Log", content);
    }

    public static void attachText(String name, String content) {
        if (content == null) return;
        Allure.addAttachment(name, "text/plain",
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), ".txt");
    }

    public static void attachHtml(String name, String html) {
        if (html == null) return;
        Allure.addAttachment(name, "text/html",
                new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), ".html");
    }

    // ========================================================================
    // ✅ SCREENSHOT ATTACHMENTS
    // ========================================================================

    public static void attachScreenshot(WebDriver driver, String name) {
        if (driver == null) {
            log.warn("Driver is null, cannot attach screenshot");
            return;
        }

        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(bytes), ".png");
        } catch (Exception e) {
            log.error("❌ Failed to attach screenshot", e);
        }
    }

    public static void attachElementScreenshot(WebElement element, WebDriver driver, String name) {
        if (driver == null || element == null) return;

        try {
            byte[] bytes = element.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(bytes), ".png");
        } catch (Exception e) {
            log.error("❌ Failed to attach element screenshot", e);
        }
    }

    public static void attachFullPageScreenshot(WebDriver driver, String name) {
        if (driver == null) {
            log.warn("Driver is null, cannot attach full page screenshot");
            return;
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(200))
                    .takeScreenshot(driver);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot.getImage(), "png", baos);

            Allure.addAttachment(name + " - Full Page", "image/png",
                    new ByteArrayInputStream(baos.toByteArray()), ".png");

        } catch (Exception e) {
            log.error("❌ Failed to attach full page screenshot", e);
        }
    }

    private static String pretty(String json) {
        try {
            json = json.trim();
            if (!json.startsWith("{") && !json.startsWith("[")) return json;

            Object parsed = new com.google.gson.Gson().fromJson(json, Object.class);
            return new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(parsed);

        } catch (Exception ignored) {
            return json;
        }
    }
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
