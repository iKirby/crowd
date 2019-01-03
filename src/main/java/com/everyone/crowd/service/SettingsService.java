package com.everyone.crowd.service;

import java.util.Map;

public interface SettingsService {

    String get(String key, String defaultValue);

    void delete(String key);

    void set(String key, String value);

    void setAll(Map<String, String> settings);

    Map<String, String> getAll();

    Map<String, String> get(String... varargs);
}
