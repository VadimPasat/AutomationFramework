package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileWriter;
import java.time.ZonedDateTime;

@Log4j2
public class DynamicExecutorWriter {

    private static final String PATH = "target/allure-results/executor.json";

    public static void writeDynamicExecutor() {
        try {
            boolean isCI = isCI();

            String buildName = isCI
                    ? "Jenkins Build #" + env("BUILD_NUMBER")
                    : "Local Run";

            String buildUrl = isCI
                    ? env("BUILD_URL")
                    : "file:///" + new File("target/allure-report/index.html")
                                   .getAbsolutePath()
                                   .replace("\\", "/");

            String json = String.format("""
                    {
                      "name": "Vadim QA Framework",
                      "type": "%s",
                      "buildName": "%s",
                      "buildUrl": "%s",
                      "reportName": "Automation Execution",
                      "timestamp": "%s",
                      "gitBranch": "%s",
                      "gitCommit": "%s"
                    }
                    """,
                    isCI ? "Jenkins" : "Local",
                    buildName,
                    buildUrl,
                    ZonedDateTime.now(),
                    env("GIT_BRANCH"),
                    env("GIT_COMMIT")
            );

            File file = new File(PATH);
            file.getParentFile().mkdirs();

            try (FileWriter fw = new FileWriter(file, false)) {
                fw.write(json);
            }

            log.info("✅ executor.json generated");

        } catch (Exception e) {
            log.error("❌ Failed to write executor.json", e);
        }
    }

    private static boolean isCI() {
        return System.getenv("CI") != null || System.getenv("BUILD_NUMBER") != null;
    }

    private static String env(String key) {
        return System.getenv().getOrDefault(key, "N/A");
    }
}