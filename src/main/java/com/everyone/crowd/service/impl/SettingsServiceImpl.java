package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.SettingsMapper;
import com.everyone.crowd.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void delete(String key) {
        settingsMapper.delete(key);
    }

    @Override
    public void set(String key, String value) {
        if (settingsMapper.get(key) != null) {
            settingsMapper.set(key, value);
        } else {
            settingsMapper.put(key, value);
        }

    }
}
