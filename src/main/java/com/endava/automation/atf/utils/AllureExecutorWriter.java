package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Files;

@Log4j2
public final class AllureExecutorWriter {

    private static final String RESULTS =
            "target/allure-results";

    private AllureExecutorWriter() {
    }

    public static void writeExecutor() {

        try {

            File executor =
                    new File(RESULTS + "/executor.json");

            String buildNumber =
                    env("BUILD_NUMBER", "1");

            String jobName =
                    env("JOB_NAME", "Local Execution");

            String buildUrl =
                    env("BUILD_URL", "http://localhost");

            String jenkinsUrl =
                    env("JENKINS_URL", "http://localhost");

            String json = String.format("""
                    {
                      "name": "%s",
                      "type": "%s",
                      "url": "%s",
                      "buildOrder": %s,
                      "buildName": "Build #%s",
                      "buildUrl": "%s",
                      "reportName": "Automation Execution Report",
                      "reportUrl": "%s/allure"
                    }
                    """,
                    jobName,
                    isCI() ? "jenkins" : "local",
                    jenkinsUrl,
                    buildNumber,
                    buildNumber,
                    buildUrl,
                    buildUrl
            );

            Files.writeString(executor.toPath(), json);

            log.info("executor.json generated");

        } catch (Exception e) {

            log.error("Failed writing executor.json", e);
        }
    }

    private static boolean isCI() {

        return System.getenv("JENKINS_URL") != null;
    }

    private static String env(
            String key,
            String defaultValue
    ) {

        String value = System.getenv(key);

        return value == null
                ? defaultValue
                : value;
    }
}