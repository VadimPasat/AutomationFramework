package com.endava.automation.atf.runner;

import org.junit.Test;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        strict = true,
        junit = "--step-notifications",
        glue = {"com/endava/automation/atf/steps",
                "com/endava/automation/atf/hook"},
        features = {"src/test/java/com/endava/automation/atf/features"},
        tags = {"@Run"}
)
public class AtfRunner {

}