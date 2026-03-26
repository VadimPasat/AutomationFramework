package com.endava.automation.atf.utils;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public final class AllureHistoryWriter {

    private static final String SOURCE = "target/allure-report/history";
    private static final String TARGET = "target/allure-results/history";

    public static void copyHistory() {
        File src = new File(SOURCE);
        File dest = new File(TARGET);

        if (!src.exists()) {
            return; // first run → no trend yet
        }

        try {
            FileUtils.copyDirectory(src, dest);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy Allure history", e);
        }
    }
}