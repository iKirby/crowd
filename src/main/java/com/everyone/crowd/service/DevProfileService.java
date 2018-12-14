package com.everyone.crowd.service;

import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface DevProfileService {
    DevProfile findById(Integer user_id);

    Map<Integer, String> getIdNameMap();

    Page<DevProfile> findAll(int pageSize, int page);

    Page<DevProfile> findByName(String name, int pageSize, int page);

    void insert(DevProfile devProfile);

    void update(DevProfile devProfile);

    void delete(Integer user_id);

    void updateStatus(Integer user_id, String status);

    void updateLevel(Integer user_id, int level);

    Map<Integer, String> getIdNameMap(List<Integer> ids);
}
