package com.everyone.crowd.service;

import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.DevProfile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;


public interface CustomerProfileService {
    CustomerProfile findById(Integer user_id);

    void insert(CustomerProfile customerProfile);

    void update(CustomerProfile customerProfile);

    void delete(Integer user_id);

    void updateStatus(Integer user_id, String status);

    void updateLevel(Integer user_id, int level);
}
