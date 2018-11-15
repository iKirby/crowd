package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.CustomerProfileMapper;
import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {
    private final CustomerProfileMapper customerProfileMapper;

    @Autowired
    public CustomerProfileServiceImpl(CustomerProfileMapper customerProfileMapper) {
        this.customerProfileMapper = customerProfileMapper;
    }

    @Override
    public CustomerProfile findById(Integer user_id) {
        return customerProfileMapper.findById(user_id);
    }

    @Override
    @Transactional
    public void insert(CustomerProfile customerProfile) {
        customerProfileMapper.insert(customerProfile);
    }

    @Override
    @Transactional
    public void update(CustomerProfile customerProfile) {
        customerProfileMapper.update(customerProfile);
    }

    @Override
    @Transactional
    public void delete(Integer user_id) {
        customerProfileMapper.delete(user_id);
    }

    @Override
    @Transactional
    public void updateStatus(String status, Integer user_id) {
        customerProfileMapper.updateStatus(status, user_id);
    }

    @Override
    @Transactional
    public void updateLevel(int level, Integer user_id) {
        customerProfileMapper.updateLevel(level, user_id);
    }
}
