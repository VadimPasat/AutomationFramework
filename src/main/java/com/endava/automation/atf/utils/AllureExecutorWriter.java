package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Log4j2
public final class AllureExecutorWriter {

    private static final String RESULTS_DIRECTORY =
            "target/allure-results";

    private static final String EXECUTOR_FILE =
            "executor.json";

    private AllureExecutorWriter() {
        // utility class
    }

    public static void writeExecutor() {

        try {

            boolean ciExecution = isCiExecution();

            File resultsDir =
                    new File(RESULTS_DIRECTORY);

            if (!resultsDir.exists()) {
                resultsDir.mkdirs();
            }

            File executorFile =
                    new File(resultsDir, EXECUTOR_FILE);

            String json =
                    ciExecution
                            ? buildCiExecutorJson()
                            : buildLocalExecutorJson();

            Files.writeString(
                    executorFile.toPath(),
                    json,
                    StandardCharsets.UTF_8
            );

            log.info("executor.json generated successfully");

        } catch (Exception e) {

            log.error(
                    "Failed generating executor.json",
                    e
            );
        }
    }

    // =====================================================
    // LOCAL EXECUTION
    // =====================================================

    private static String buildLocalExecutorJson() {

        return """
                {
                  "name": "Local Execution",
                  "type": "local",
                  "buildName": "Local Run",
                  "reportName": "UI + API Regression Suite"
                }
                """;
    }

    // =====================================================
    // JENKINS EXECUTION
    // =====================================================

    private static String buildCiExecutorJson() {

        String buildNumber =
                env("BUILD_NUMBER", "1");

        String buildUrl =
                env("BUILD_URL", "");

        String reportUrl =
                buildUrl + "allure";

        String jobName =
                env(
                        "JOB_NAME",
                        "Automation Regression"
                );

        return String.format("""
                {
                  "name": "Jenkins",
                  "type": "jenkins",
                  "buildOrder": %s,
                  "buildName": "Build #%s",
                  "buildUrl": "%s",
                  "reportUrl": "%s",
                  "reportName": "%s"
                }
                """,
                buildNumber,
                buildNumber,
                buildUrl,
                reportUrl,
                jobName
        );
    }

    // =====================================================
    // EXECUTION TYPE
    // =====================================================

    private static boolean isCiExecution() {

        return System.getenv("JENKINS_URL") != null
                || System.getenv("BUILD_NUMBER") != null;
    }

    // =====================================================
    // ENVIRONMENT VARIABLE
    // =====================================================

    private static String env(
            String key,
            String defaultValue
    ) {

        String value = System.getenv(key);

        return value == null || value.isBlank()
                ? defaultValue
                : value;
    }
}