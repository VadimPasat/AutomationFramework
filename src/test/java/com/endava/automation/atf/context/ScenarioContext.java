package com.endava.automation.atf.context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private final Map<String, Object> context;
    private static ScenarioContext scenarioContext;

    private ScenarioContext() {
        context = new HashMap<String, Object>();
    }

    public static ScenarioContext getScenarioContext() {
        if (scenarioContext == null) {
            scenarioContext = new ScenarioContext();
        }
        return scenarioContext;
    }

    public void saveData(String key, Object value) {
        context.put(key, value);
    }

    public Object getData(String key) {
        return context.get(key);
    }

    public void clearData() {
        context.clear();
    }
}
