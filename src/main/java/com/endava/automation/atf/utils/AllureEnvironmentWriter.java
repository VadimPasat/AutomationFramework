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

    /**
     * ✅ SAFE, parallel-friendly environment file writer
     * ✅ Will NOT overwrite if file already exists
     * ✅ Works for both UI & API runners
     */
    public static void safeWriteEnvironment() {

        File resultsDir = new File(ALLURE_RESULTS_PATH);
        File envFile = new File(resultsDir, ENV_FILE_NAME);

        // ✅ Prevent overwriting / conflicts in parallel UI/API runners
        if (envFile.exists()) {
            log.info("ℹ environment.properties already exists → skipping write");
            return;
        }

        try {
            if (!resultsDir.exists() && resultsDir.mkdirs()) {
                log.info("Created Allure results directory: {}", ALLURE_RESULTS_PATH);
            }

            ConfigFileReader config = new ConfigFileReader();
            Properties envProps = new Properties();

            // ✅ Framework Info
            envProps.setProperty("Browser", config.getBrowser().name());
            envProps.setProperty("Environment", config.getEnvironment());
            envProps.setProperty("Base URL", config.getBaseUrl());
            envProps.setProperty("Headless", config.getHeadLessMode().toString());
            envProps.setProperty("Window Maximize", config.getWindowMaximize().toString());
            envProps.setProperty("Implicit Wait", String.valueOf(config.getImplicitlyWait()));

            // ✅ System Info
            envProps.setProperty("OS", System.getProperty("os.name"));
            envProps.setProperty("User", System.getProperty("user.name"));
            envProps.setProperty("Java Version", System.getProperty("java.version"));

            // ✅ Optional custom value (Tester)
            try {
                Object tester = config.getClass()
                        .getMethod("getTester")
                        .invoke(config);

                if (tester != null) {
                    envProps.setProperty("Tester", tester.toString());
                }
            } catch (Exception ignored) {
                // ignore optional field
            }

            // ✅ Write file safely
            try (FileOutputStream fos = new FileOutputStream(envFile)) {
                envProps.store(fos, "Allure Environment");
            }

            log.info("✅ environment.properties generated at: {}", envFile.getAbsolutePath());

        } catch (IOException e) {
            log.error("❌ Failed to write Allure environment.properties", e);
            throw new RuntimeException("Failed to write Allure environment.properties", e);
        }
    }
}