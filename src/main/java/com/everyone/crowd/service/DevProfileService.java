package com.everyone.crowd.service;

import com.everyone.crowd.entity.DevProfile;
import org.springframework.stereotype.Service;

public interface DevProfileService {
    DevProfile findById(Integer user_id);

    void insert(DevProfile devProfile);

    void update(DevProfile devProfile);

    void delete(Integer user_id);

    void updateStatus(String status, Integer user_id);

    void updateLevel(int level, Integer user_id);
}
