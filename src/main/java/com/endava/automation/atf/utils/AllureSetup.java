package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;

@Log4j2
public final class AllureSetup {

    private static volatile boolean initialized = false;

    private AllureSetup() {
    }

    public static synchronized void init() {

        if (initialized) {
            return;
        }
        try {

            log.info("Initializing Allure infrastructure...");
            AllureHistoryManager.restoreHistory();
            AllureExecutorWriter.writeExecutor();
            AllureEnvironmentWriter.writeEnvironment();
            initialized = true;
            log.info("Allure initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Allure", e);
            throw new RuntimeException(e);
        }
    }

    private static void cleanResultsDirectory() {
        try {

            File results =
                    new File("target/allure-results");

            if (results.exists()) {
                FileUtils.deleteDirectory(results);
            }
            results.mkdirs();
            log.info("allure-results cleaned");
        } catch (Exception e) {
            log.error("Failed cleaning allure-results", e);
        }
    }
}