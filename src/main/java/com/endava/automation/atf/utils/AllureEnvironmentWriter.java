package com.endava.automation.atf.utils;

import com.endava.automation.atf.configreader.ConfigFileReader;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

@Log4j2
public final class AllureEnvironmentWriter {

    private static final String RESULTS =
            "target/allure-results";

    private AllureEnvironmentWriter() {
    }

    public static void writeEnvironment() {

        try {

            ConfigFileReader config =
                    new ConfigFileReader();

            Properties properties =
                    new Properties();

            // =====================================================
            // FRAMEWORK CONFIGURATION
            // =====================================================

            properties.setProperty(
                    "Browser",
                    systemOrConfig(
                            "browser",
                            config.getBrowser().name()
                    )
            );

            properties.setProperty(
                    "Environment",
                    systemOrConfig(
                            "env",
                            config.getEnvironment()
                    )
            );

            properties.setProperty(
                    "Base URL",
                    config.getBaseUrl()
            );

            properties.setProperty(
                    "Headless",
                    String.valueOf(config.getHeadLessMode())
            );

            properties.setProperty(
                    "Window Maximize",
                    String.valueOf(config.getWindowMaximize())
            );

            properties.setProperty(
                    "Implicit Wait",
                    String.valueOf(config.getImplicitlyWait())
            );

            // =====================================================
            // EXECUTION METADATA
            // =====================================================

            properties.setProperty(
                    "Tester",
                    systemOrConfig(
                            "tester",
                            "Vadim Pasat"
                    )
            );

            properties.setProperty(
                    "Execution Type",
                    systemOrConfig(
                            "execution.type",
                            "local"
                    )
            );

            properties.setProperty(
                    "Build Number",
                    systemOrConfig(
                            "build.number",
                            "local-run"
                    )
            );

            properties.setProperty(
                    "Pipeline",
                    systemOrConfig(
                            "pipeline.name",
                            "local"
                    )
            );

            // =====================================================
            // SYSTEM INFORMATION
            // =====================================================

            properties.setProperty(
                    "OS",
                    System.getProperty("os.name")
            );

            properties.setProperty(
                    "Java Version",
                    System.getProperty("java.version")
            );

            properties.setProperty(
                    "User",
                    System.getProperty("user.name")
            );

            // =====================================================
            // WRITE FILE
            // =====================================================

            File envFile =
                    new File(RESULTS + "/environment.properties");

            try (FileOutputStream fos =
                         new FileOutputStream(envFile)) {

                properties.store(fos, "Allure Environment");
            }

            log.info("environment.properties generated");

        } catch (Exception e) {

            log.error(
                    "Failed writing environment.properties",
                    e
            );
        }
    }

    /**
     * Priority:
     * 1. JVM parameter (-D)
     * 2. Config value
     */
    private static String systemOrConfig(
            String systemKey,
            String configValue
    ) {

        return System.getProperty(
                systemKey,
                configValue
        );
    }
}