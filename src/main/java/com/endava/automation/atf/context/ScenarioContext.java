package com.endava.automation.atf.context;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ScenarioContext {

    private static final ThreadLocal<ScenarioContext> THREAD_LOCAL =
            ThreadLocal.withInitial(ScenarioContext::new);

    private final Map<String, Object> data = new ConcurrentHashMap<>();

    private ScenarioContext() {}

    public static ScenarioContext get() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    public <T> void set(String key, T value) {
        validateKey(key);
        data.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        validateKey(key);
        Object value = data.get(key);
        return value == null ? null : type.cast(value);
    }

    public Optional<Object> getOptional(String key) {
        validateKey(key);
        return Optional.ofNullable(data.get(key));
    }

    public void clearData() {
        data.clear();
    }

    private void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("ScenarioContext key cannot be null or empty");
        }
    }
}