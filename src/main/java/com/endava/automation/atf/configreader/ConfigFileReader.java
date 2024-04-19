package com.endava.automation.atf.configreader;

import com.endava.automation.atf.constant.DriverType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String PROPERTY_FILE_PATH = "src/main/resources/config.properties";


    public ConfigFileReader() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PROPERTY_FILE_PATH))) {
            properties = new Properties();
            properties.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("configuration.properties not found at " + PROPERTY_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDriverPath() {
        String driverPath = properties.getProperty("driverPath");
        if (!driverPath.isEmpty())
            return driverPath;
        else
            throw new RuntimeException("Driver Path not specified in the configuration.properties file for the Key:driverPath");
    }

    public long getImplicitlyWait() {
        String implicitlyWait = properties.getProperty("implicitlyWait");
        if (!implicitlyWait.isEmpty()) {
            try {
                return Long.parseLong(implicitlyWait);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Not able to parse value : " + implicitlyWait + " int to Long");
            }
        }
        return 30;
    }

    public DriverType getBrowser() {
        try {
            String browserName = properties.getProperty("browser").toUpperCase();
            return DriverType.valueOf(browserName);
        } catch (NullPointerException e) {
            throw new RuntimeException("Missing browser property");
        }
    }

    public Boolean getBrowserWindowSize() {
        String windowSize = properties.getProperty("windowMaximize");
        return Boolean.valueOf(windowSize);
    }

    public String getScreenShotSaveDirectoryPath() {
        return properties.getProperty("screenShotsPath").trim().toLowerCase();
    }

    public boolean confirmScreenShot() {
        return Boolean.parseBoolean(properties.getProperty("takeScreenShot").trim().toLowerCase());
    }
}
