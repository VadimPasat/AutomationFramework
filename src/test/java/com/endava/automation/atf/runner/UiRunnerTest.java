package com.endava.automation.atf.runner;

import com.endava.automation.atf.utils.AllureSetup;
import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")

@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.endava.automation.atf.stepdef," +
                "com.endava.automation.atf.hook"
)

@ConfigurationParameter(
        key = FILTER_TAGS_PROPERTY_NAME,
        value = "@UI"
)

@ConfigurationParameter(
        key = PARALLEL_EXECUTION_ENABLED_PROPERTY_NAME,
        value = "true"
)
@ConfigurationParameter(
        key = PARALLEL_CONFIG_STRATEGY_PROPERTY_NAME,
        value = "fixed"
)
@ConfigurationParameter(
        key = PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME,
        value = "4"
)

@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)

// ✅ REQUIRED for Allure report to be generated correctly
@ConfigurationParameter(
        key = "allure.results.directory",
        value = "target/allure-results"
)


public class UiRunnerTest {

    static {
        AllureSetup.init();
    }
}