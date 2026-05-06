package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileWriter;

@Log4j2
public class AllureExecutorWriter {

    private static final String OUTPUT_PATH = "target/allure-results/executor.json";

    public static void safeWriteExecutor() {
        try {
            File file = new File(OUTPUT_PATH);

            if (file.exists()) {
                log.info("ℹ executor.json already exists → skipping");
                return;
            }

            file.getParentFile().mkdirs();

            String json = """
                    {
                      "name": "Vadim QA Framework",
                      "type": "local",
                      "buildName": "UI Regression Run",
                      "buildUrl": "http://localhost",
                      "reportName": "Automation Practice Execution"
                    }
            """;

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
            }

            log.info("✅ executor.json created");

        } catch (Exception e) {
            log.error("❌ Failed to write executor.json", e);
        }
    }
}