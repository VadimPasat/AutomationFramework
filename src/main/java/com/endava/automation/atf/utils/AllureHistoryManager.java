package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;

@Log4j2
public class AllureHistoryManager {

    private static final String EXTERNAL_HISTORY = "allure-history";
    private static final String RESULTS_HISTORY = "target/allure-results/history";
    private static final String REPORT_HISTORY = "target/allure-report/history";

    // ✅ BEFORE TESTS → restore history
    public static void restoreHistory() {
        try {
            File external = new File(EXTERNAL_HISTORY);
            File results = new File(RESULTS_HISTORY);

            if (!external.exists() || isEmpty(external)) {
                File reportHistory = new File(REPORT_HISTORY);

                if (reportHistory.exists() && !isEmpty(reportHistory)) {
                    log.info("♻ Bootstrapping history from previous report...");
                    FileUtils.copyDirectory(reportHistory, external);
                } else {
                    log.info("ℹ No history yet (first run)");
                    return;
                }
            }

            if (results.exists()) {
                FileUtils.deleteDirectory(results);
            }

            FileUtils.copyDirectory(external, results);

            log.info("✅ History restored (trend enabled)");

        } catch (Exception e) {
            log.error("❌ Failed to restore history", e);
        }
    }

    // ✅ AFTER REPORT → save history
    public static void saveHistory() {
        try {
            File reportHistory = new File(REPORT_HISTORY);
            File external = new File(EXTERNAL_HISTORY);

            if (!reportHistory.exists() || isEmpty(reportHistory)) {
                log.warn("⚠ No report history to save");
                return;
            }

            if (!external.exists()) {
                external.mkdirs();
            }

            FileUtils.copyDirectory(reportHistory, external);

            log.info("✅ History saved (trend preserved)");

        } catch (Exception e) {
            log.error("❌ Failed to save history", e);
        }
    }

    private static boolean isEmpty(File dir) {
        return dir.listFiles() == null || dir.listFiles().length == 0;
    }

    // 🔥 REQUIRED for Maven execution
    public static void main(String[] args) {
        if (args.length > 0 && "save".equalsIgnoreCase(args[0])) {
            saveHistory();
        } else {
            restoreHistory();
        }
    }
}