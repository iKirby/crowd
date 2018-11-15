package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.DevProfileMapper;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.service.DevProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DevProfileServiceImpl implements DevProfileService {
    private final DevProfileMapper devProfileMapper;

    @Autowired
    public DevProfileServiceImpl(DevProfileMapper devProfileMapper) {
        this.devProfileMapper = devProfileMapper;
    }

    @Override
    public DevProfile findById(Integer user_id) {
        return devProfileMapper.findById(user_id);
    }

    @Override
    @Transactional
    public void insert(DevProfile devProfile) {
        devProfileMapper.insert(devProfile);
    }

    @Override
    @Transactional
    public void update(DevProfile devProfile) {
        devProfileMapper.update(devProfile);
    }

    @Override
    @Transactional
    public void delete(Integer user_id) {
        devProfileMapper.delete(user_id);
    }

    @Override
    @Transactional
    public void updateStatus(String status, Integer user_id) {
        devProfileMapper.updateStatus(status, user_id);
    }

    @Override
    @Transactional
    public void updateLevel(int level, Integer user_id) {
        devProfileMapper.updateLevel(level, user_id);
    }
}

