package com.endava.automation.atf.datagenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public class DataGenerator {

    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "qQwWeErRtTyYuUiIoOpPaAsSdDfFgGhHjJkKlLzZxXcCvVbBnNmM";

    // Screen Shot name generator based on current date and time
    public static String screenShotNameGenerator() {
        return "IMG_" + generateNumericName();
    }

    // Folder name generator based on current date and time
    public static String folderNameGenerator() {
        return "FDR_" + generateNumericName();
    }

    private static String generateNumericName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    }

    public static String generateRandomString(int stringLength) {
        return "Test" + lettersRandomizer(stringLength) + generateRandomNumber();
    }

    public static String generateRandomEmail(int stringLength) {
        return (lettersRandomizer(stringLength) + generateRandomNumber() + "@" +
                lettersRandomizer(varRandomizer(5)) +
                "." + lettersRandomizer(2)).toLowerCase();
    }

    private static int varRandomizer(int stringLength) {
        return RANDOM.nextInt(stringLength) + 2;
    }

    private static String lettersRandomizer(int x) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < x; i++) {
            result.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return result.toString();
    }

    public static String generateRandomNumber() {
        return "" + RANDOM.nextInt(9999);
    }
}