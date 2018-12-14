package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.*;
import com.everyone.crowd.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StatServiceImpl implements StatService {
    private final DemandMapper demandMapper;
    private final DevProfileMapper devProfileMapper;
    private final CustomerProfileMapper customerProfileMapper;
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final FeedbackMapper feedbackMapper;

    @Autowired
    public StatServiceImpl(DemandMapper demandMapper, DevProfileMapper devProfileMapper,
                           CustomerProfileMapper customerProfileMapper, UserMapper userMapper,
                           OrderMapper orderMapper, FeedbackMapper feedbackMapper) {
        this.demandMapper = demandMapper;
        this.devProfileMapper = devProfileMapper;
        this.customerProfileMapper = customerProfileMapper;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public int getDemandsCount(Date startDate) {
        return demandMapper.countByTimeAfter(startDate);
    }

    @Override
    public int getOrdersCount(Date startDate) {
        return orderMapper.countByTimeAfter(startDate);
    }

    @Override
    public int getDevelopersCount(String status) {
        return devProfileMapper.countByStatus(status);
    }

    @Override
    public int getCustomersCount(String status) {
        return customerProfileMapper.countByStatus(status);
    }

    @Override
    public int getUsersCount() {
        return userMapper.getUserCount();
    }

    @Override
    public int getFeedbacksCount() {
        return feedbackMapper.countNotReplied();
    }
}
