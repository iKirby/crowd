package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.DevProfileMapper;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.service.DevProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void updateStatus(Integer user_id, String status) {
        devProfileMapper.updateStatus(user_id, status);
    }

    @Override
    @Transactional
    public void updateLevel(Integer user_id, int level) {
        devProfileMapper.updateLevel(user_id, level);
    }

    @Override
    public Map<Integer, String> getIdNameMap(List<Integer> ids) {
        List<DevProfile> devProfiles = devProfileMapper.findByIds(ids);
        Map<Integer, String> map = new HashMap<>();
        for (DevProfile devProfile : devProfiles) {
            map.put(devProfile.getUserId(), devProfile.getName());
        }
        return map;
    }
}

