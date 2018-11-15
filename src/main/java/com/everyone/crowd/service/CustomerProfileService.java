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
    void updateStatus (String status,Integer user_id);
    void updateLevel (int level,Integer user_id);
}
