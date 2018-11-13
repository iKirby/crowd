package com.everyone.crowd.service;

public interface SettingsService {

    String get(String key, String defaultValue);

    void delete(String key);

    void set(String key, String value);
}
