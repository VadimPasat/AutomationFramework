package com.endava.automation.atf.configreader;

import com.endava.automation.atf.constant.DriverType;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class ConfigFileReader {

    private final Properties properties;

    public ConfigFileReader() {
        properties = new Properties();

        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    // ================= ENV =================

    private String getEnv() {
        String env = System.getProperty("env");

        if (env == null || env.isBlank()) {
            env = properties.getProperty("env", "qa");
        }

        log.info("Running tests on ENV: {}", env);
        return env;
    }

    public String getBaseUrl() {
        String key = "base.url." + getEnv();
        String url = properties.getProperty(key);

        if (url == null || url.isBlank()) {
            throw new RuntimeException("Base URL not found for key: " + key);
        }

        log.info("Using BASE URL: {}", url);
        return url;
    }

    public String getBaseApiUrl() {
        String key = "base.api.url";
        String url = properties.getProperty(key);

        if (url == null || url.isBlank()) {
            throw new RuntimeException("Base URL not found for key: " + key);
        }

        log.info("Using BASE URL: {}", url);
        return url;
    }

    // ================= DRIVER =================

    public String getDriverPath() {
        String driverPath = properties.getProperty("driverPath");

        if (driverPath == null || driverPath.isBlank()) {
            throw new RuntimeException("Driver Path not specified in config.properties");
        }

        return driverPath;
    }

    public long getImplicitlyWait() {
        String value = properties.getProperty("implicitlyWait", "30");

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid implicitlyWait value: " + value);
        }
    }

    public DriverType getBrowser() {
        String browser = System.getProperty("browser",
                properties.getProperty("browser", "CHROME"));

        try {
            return DriverType.valueOf(browser.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid browser: " + browser);
        }
    }

    public Boolean getHeadLessMode() {
        return Boolean.parseBoolean(
                System.getProperty("headless",
                        properties.getProperty("headless", "false"))
        );
    }

    public Boolean getWindowMaximize() {
        return Boolean.parseBoolean(
                System.getProperty("windowMaximize",
                        properties.getProperty("windowMaximize", "true"))
        );
    }

    public String getEnvironment() {
        String driverPath = properties.getProperty("env");

        if (driverPath == null || driverPath.isBlank()) {
            throw new RuntimeException("Environment is not specified in config.properties");
        }

        return driverPath;
    }

    // ================= SCREENSHOT =================

    public String getScreenShotSaveDirectoryPath() {
        return properties.getProperty("screenShotsPath", "screenshots").trim();
    }

    public boolean confirmScreenShot() {
        return Boolean.parseBoolean(
                properties.getProperty("takeScreenShot", "true").trim()
        );
    }
}