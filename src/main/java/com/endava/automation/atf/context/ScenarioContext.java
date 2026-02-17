package com.endava.automation.atf.context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private static final ThreadLocal<ScenarioContext> threadLocalContext = ThreadLocal.withInitial(ScenarioContext::new);

    private final Map<String, Object> context;

    private ScenarioContext() {
        context = new HashMap<>();
    }

    public static ScenarioContext getScenarioContext() {
        return threadLocalContext.get();
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

    public Object getDataOrDefault(String key, Object defaultValue) {
        return context.getOrDefault(key, defaultValue);
    }
}
