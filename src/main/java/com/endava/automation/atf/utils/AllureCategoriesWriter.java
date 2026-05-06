package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileWriter;

@Log4j2
public class AllureCategoriesWriter {

    private static final String OUTPUT_PATH = "target/allure-results/categories.json";

    public static void safeWriteCategories() {
        try {
            File file = new File(OUTPUT_PATH);

            if (file.exists()) {
                log.info("ℹ categories.json already exists → skipping");
                return;
            }

            file.getParentFile().mkdirs();

            String json = """
            [
              { "name": "Failed Tests", "matchedStatuses": ["failed"] },
              { "name": "Broken Tests", "matchedStatuses": ["broken"] }
            ]
            """;

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
            }

            log.info("✅ categories.json created");

        } catch (Exception e) {
            log.error("❌ Failed to write categories.json", e);
        }
    }
}