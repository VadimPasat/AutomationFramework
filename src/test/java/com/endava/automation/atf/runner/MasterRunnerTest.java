package com.endava.automation.atf.runner;

import com.endava.automation.atf.utils.AllureSetup;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UiRunnerTest.class,
        ApiRunnerTest.class
})
public class MasterRunnerTest {

    static {
        AllureSetup.init();
    }
}