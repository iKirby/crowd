package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.CustomerProfileMapper;
import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Page<CustomerProfile> findAll(int pageSize, int page) {
        int total = customerProfileMapper.countAll();
        List<CustomerProfile> content = customerProfileMapper.findAll(pageSize * (page - 1), pageSize);
        Page<CustomerProfile> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    public Page<CustomerProfile> findByName(String name, int pageSize, int page) {
        int total = customerProfileMapper.countByName(name);
        List<CustomerProfile> content = customerProfileMapper.findByName(name, pageSize * (page - 1), pageSize);
        Page<CustomerProfile> customerProfilePage = new Page<>();
        customerProfilePage.setContent(content);
        customerProfilePage.setCurrentPage(page);
        customerProfilePage.setTotal(total);
        customerProfilePage.setPageSize(pageSize);
        return customerProfilePage;
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
    public void updateStatus(Integer user_id, String status) {
        customerProfileMapper.updateStatus(user_id, status);
    }

    @Override
    @Transactional
    public void updateLevel(Integer user_id, int level) {
        customerProfileMapper.updateLevel(user_id, level);
    }

    @Override
    public Map<Integer, String> getIdNameMap(List<Integer> ids) {
        List<CustomerProfile> customerProfiles = customerProfileMapper.findByIds(ids);
        Map<Integer, String> map = new HashMap<>();
        for (CustomerProfile customerProfile : customerProfiles) {
            map.put(customerProfile.getUserId(), customerProfile.getName());
        }
        return map;
    }
}
