package com.endava.automation.atf.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "json:target/cucumber-reports/cucumber.json",
                "rerun:target/cucumber-reports/rerun.txt",
                "html:target/cucumber-reports/cucumber-html-report.html"

        },
        strict = true,
        junit = "--step-notifications",
        glue = {"com/endava/automation/atf/stepdef",
                "com/endava/automation/atf/apitests",
                "com/endava/automation/atf/hook"},
        features = {"src/test/java/com/endava/automation/atf/features"},
        tags = {"@Run"}
)
public class AtfRunner {
}