package com.everyone.crowd.service;

import com.everyone.crowd.entity.CustomerProfile;

import java.util.List;
import java.util.Map;


public interface CustomerProfileService {
    CustomerProfile findById(Integer user_id);

    void insert(CustomerProfile customerProfile);

    void update(CustomerProfile customerProfile);

    void delete(Integer user_id);

    void updateStatus(Integer user_id, String status);

    void updateLevel(Integer user_id, int level);

    Map<Integer, String> getIdNameMap(List<Integer> ids);
}
