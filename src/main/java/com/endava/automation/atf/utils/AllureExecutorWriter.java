package com.endava.automation.atf.utils;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.Files;

@Log4j2
public final class AllureExecutorWriter {

    private static final String ALLURE_RESULTS_PATH = "target/allure-results";
    private static final String SOURCE_FILE = "allure/executor.json";
    private static final String TARGET_FILE = "executor.json";

    private AllureExecutorWriter() {}

    public static void writeExecutor() {
        copyFileFromResources(SOURCE_FILE, TARGET_FILE);
    }

    private static void copyFileFromResources(String resourcePath, String targetFileName) {
        try (InputStream input = AllureExecutorWriter.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (input == null) {
                throw new RuntimeException(resourcePath + " not found in resources");
            }

            File dir = new File(ALLURE_RESULTS_PATH);
            if (!dir.exists() && dir.mkdirs()) {
                log.info("Created Allure results directory");
            }

            File targetFile = new File(dir, targetFileName);

            Files.copy(input, targetFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            log.info("{} copied to {}", resourcePath, targetFile.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Failed to copy " + resourcePath, e);
        }
    }
}