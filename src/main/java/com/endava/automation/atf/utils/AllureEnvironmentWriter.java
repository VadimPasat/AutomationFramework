package com.endava.automation.atf.utils;

import com.endava.automation.atf.configreader.ConfigFileReader;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Log4j2
public final class AllureEnvironmentWriter {

    private static final String ALLURE_RESULTS_PATH = "target/allure-results";
    private static final String ENV_FILE_NAME = "environment.properties";

    private AllureEnvironmentWriter() {
        // utility class
    }

    public static void writeEnvironment() {

        ConfigFileReader config = new ConfigFileReader();
        Properties envProps = new Properties();

        // 🔥 Framework config
        envProps.setProperty("Browser", config.getBrowser().name());
        envProps.setProperty("Environment", config.getEnvironment());
        envProps.setProperty("Base URL", config.getBaseUrl());
        envProps.setProperty("Headless", config.getHeadLessMode().toString());
        envProps.setProperty("Window Maximize", config.getWindowMaximize().toString());
        envProps.setProperty("Implicit Wait", String.valueOf(config.getImplicitlyWait()));

        // 🔥 System info
        envProps.setProperty("OS", System.getProperty("os.name"));
        envProps.setProperty("User", System.getProperty("user.name"));
        envProps.setProperty("Java Version", System.getProperty("java.version"));

        // 🔥 Optional (if you added in config)
        try {
            envProps.setProperty("Tester", config.getClass()
                    .getMethod("getTester")
                    .invoke(config)
                    .toString());
        } catch (Exception ignored) {
            // optional field
        }

        writeToFile(envProps);
    }

    private static void writeToFile(Properties envProps) {
        File resultsDir = new File(ALLURE_RESULTS_PATH);

        if (!resultsDir.exists() && resultsDir.mkdirs()) {
            log.info("Created Allure results directory: {}", ALLURE_RESULTS_PATH);
        }

        File file = new File(resultsDir, ENV_FILE_NAME);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            envProps.store(fos, "Allure Environment");
            log.info("Allure environment.properties generated at: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to write Allure environment.properties", e);
            throw new RuntimeException("Failed to write Allure environment.properties", e);
        }
    }
}