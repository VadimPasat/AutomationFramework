package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;

@Log4j2
public final class AllureHistoryManager {

    private static final String HISTORY_STORAGE =
            "allure-history/history";

    private static final String RESULTS_HISTORY =
            "target/allure-results/history";

    private static final String REPORT_HISTORY =
            "target/allure-report/history";

    private AllureHistoryManager() {
    }

    public static void restoreHistory() {

        try {

            File source =
                    new File(HISTORY_STORAGE);

            File destination =
                    new File(RESULTS_HISTORY);

            if (!source.exists()) {

                log.info("No Allure history found");

                return;
            }

            FileUtils.copyDirectory(source, destination);

            log.info("Allure history restored");

        } catch (Exception e) {
            log.error("Failed restoring Allure history", e);
        }
    }

    public static void saveHistory() {

        try {

            File source =
                    new File(REPORT_HISTORY);

            File destination =
                    new File(HISTORY_STORAGE);

            if (!source.exists()) {

                log.warn("No report history found");

                return;
            }

            if (destination.exists()) {
                FileUtils.deleteDirectory(destination);
            }

            FileUtils.copyDirectory(source, destination);

            log.info("Allure history saved");

        } catch (Exception e) {
            log.error("Failed saving Allure history", e);
        }
    }
}