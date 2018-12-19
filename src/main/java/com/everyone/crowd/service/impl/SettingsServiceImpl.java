package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.SettingsMapper;
import com.everyone.crowd.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final SettingsMapper settingsMapper;

    @Autowired
    public SettingsServiceImpl(SettingsMapper settingsMapper) {
        this.settingsMapper = settingsMapper;
    }

    @Override
    public String get(String key, String defaultValue) {
        String value = settingsMapper.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    @Transactional
    public void delete(String key) {
        settingsMapper.delete(key);
    }

    @Override
    @Transactional
    public void set(String key, String value) {
        if (settingsMapper.get(key) != null) {
            settingsMapper.set(key, value);
        } else {
            settingsMapper.put(key, value);
        }
    }

    @Override
    @Transactional
    public void setAll(Map<String, String> settings) {
        for (Map.Entry<String, String> setting : settings.entrySet()) {
            if (settingsMapper.get(setting.getKey()) != null) {
                settingsMapper.set(setting.getKey(), setting.getValue());
            } else {
                settingsMapper.put(setting.getKey(), setting.getValue());
            }
        }
    }

    @Override
    public Map<String, String> getAll() {
        List<Map> result = settingsMapper.findAll();
        Map<String, String> settings = new HashMap<>();
        for (Object o : result) {
            Map<String, String> setting = (Map<String, String>) o;
            settings.put(setting.get("key"), setting.get("value"));
        }
        return settings;
    }

    @Override
    public Map<String, String> get(String... varargs) {
        Map<String, String> settings = new HashMap<>();
        for (String key : varargs) {
            settings.put(key, settingsMapper.get(key));
        }
        return settings;
    }
}
