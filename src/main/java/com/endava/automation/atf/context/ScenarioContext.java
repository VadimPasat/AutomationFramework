package com.endava.automation.atf.context;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ScenarioContext {

    private static final ThreadLocal<ScenarioContext> threadLocalContext =
            ThreadLocal.withInitial(ScenarioContext::new);

    private final Map<String, Object> context;

    private ScenarioContext() {
        this.context = new ConcurrentHashMap<>();
    }

    public static ScenarioContext getScenarioContext() {
        return threadLocalContext.get();
    }

    public static void removeContext() {
        threadLocalContext.remove(); // Prevent memory leaks
    }

    public <T> void saveData(String key, T value) {
        validateKey(key);
        context.put(key, value);
    }

    public <T> T getData(String key, Class<T> type) {
        validateKey(key);
        Object value = context.get(key);

        if (value == null) {
            return null;
        }

        if (!type.isInstance(value)) {
            throw new ClassCastException(
                    "Expected type " + type.getName() +
                            " but found " + value.getClass().getName()
            );
        }
        return type.cast(value);
    }

    public <T> T getDataOrDefault(String key, T defaultValue, Class<T> type) {
        validateKey(key);
        Object value = context.getOrDefault(key, defaultValue);

        if (!type.isInstance(value)) {
            throw new ClassCastException(
                    "Expected type " + type.getName() +
                            " but found " + value.getClass().getName()
            );
        }
        return type.cast(value);
    }

    public Optional<Object> getOptionalData(String key) {
        validateKey(key);
        return Optional.ofNullable(context.get(key));
    }

    public void clearData() {
        context.clear();
    }

    public void reset() {
        clearData();
        removeContext();
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Context key cannot be null or empty");
        }
    }
}