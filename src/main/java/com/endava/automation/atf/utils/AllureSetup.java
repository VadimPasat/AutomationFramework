package com.endava.automation.atf.utils;

public class AllureSetup {

    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        AllureExecutorWriter.writeExecutor();
        AllureCategoriesWriter.writeCategories();
        AllureEnvironmentWriter.writeEnvironment();
        //AllureHistoryWriter.copyHistory();

        initialized = true;
    }
}