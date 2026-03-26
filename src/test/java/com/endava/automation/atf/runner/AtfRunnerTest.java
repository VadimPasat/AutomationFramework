package com.endava.automation.atf.runner;

import com.endava.automation.atf.utils.AllureSetup;
import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")

@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.endava.automation.atf.stepdef,com.endava.automation.atf.hook,com.endava.automation.atf.apitests"
)
@ConfigurationParameter(
        key = FILTER_TAGS_PROPERTY_NAME,
        value = "@UI"
)
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty,io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
public class AtfRunnerTest {

    static {
        AllureSetup.init();
    }
}