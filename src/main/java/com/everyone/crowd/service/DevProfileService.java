package com.everyone.crowd.service;

import com.everyone.crowd.entity.DevProfile;
import org.springframework.stereotype.Service;

public interface DevProfileService {
    DevProfile findById(Integer user_id);

    void insert(DevProfile devProfile);

    void update(DevProfile devProfile);

    void delete(Integer user_id);

    void updateStatus(Integer user_id, String status);

    void updateLevel(Integer user_id, int level);
}
