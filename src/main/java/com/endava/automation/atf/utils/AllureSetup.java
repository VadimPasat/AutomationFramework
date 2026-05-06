package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
public class AllureSetup {

    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        try {
            log.info("🚀 Initializing Allure setup...");

            File resultsDir = new File("target/allure-results");
            if (!resultsDir.exists()) {
                resultsDir.mkdirs();
            }

            // 🔥 STEP 1 — Restore history (TREND)
            AllureHistoryManager.restoreHistory();

            // 🔥 STEP 2 — Executor (MUST be before report)
            DynamicExecutorWriter.writeDynamicExecutor();

            // 🔥 STEP 3 — Environment
            AllureEnvironmentWriter.safeWriteEnvironment();

            // 🔥 STEP 4 — Categories
            AllureCategoriesWriter.safeWriteCategories();

            log.info("✅ Allure setup initialized");

        } catch (Exception e) {
            log.error("❌ Allure setup failed", e);
        }

        initialized = true;
    }
}