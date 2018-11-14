package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.OrderMapper;
import com.everyone.crowd.entity.Order;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public Order findById(Integer id) {
        return orderMapper.findById(id);
    }

    @Override
    public Page<Order> findByDevId(Integer devId, int pageSize, int page) {
        int total = orderMapper.countByDevId(devId);
        List<Order> content = orderMapper.findByDevId(devId, pageSize * (page - 1), page);
        Page<Order> orderPage = new Page<>();
        orderPage.setContent(content);
        orderPage.setCurrentPage(page);
        orderPage.setTotal(total);
        orderPage.setPageSize(pageSize);
        return orderPage;
    }

    @Override
    public Page<Order> findByCustomerId(Integer customerId, int pageSize, int page) {
        int total = orderMapper.countByCustomerId(customerId);
        List<Order> content = orderMapper.findByCustomerId(customerId, pageSize * (page - 1), page);
        Page<Order> orderPage = new Page<>();
        orderPage.setContent(content);
        orderPage.setCurrentPage(page);
        orderPage.setTotal(total);
        orderPage.setPageSize(pageSize);
        return orderPage;
    }

    @Override
    public Page<Order> findByStatus(String status, int pageSize, int page) {
        int total = orderMapper.countByStatus(status);
        List<Order> content = orderMapper.findByStatus(status, pageSize * (page - 1), page);
        Page<Order> orderPage = new Page<>();
        orderPage.setContent(content);
        orderPage.setCurrentPage(page);
        orderPage.setTotal(total);
        orderPage.setPageSize(pageSize);
        return orderPage;
    }

    @Override
    @Transactional
    public int place(Order order) {
        return orderMapper.insert(order);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, String status) {
        orderMapper.updateStatus(id, status);
    }

    @Override
    @Transactional
    public void completeOrder(Integer id, Date completeTime) {
        orderMapper.completeOrder(id, completeTime);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        orderMapper.delete(id);
    }
}
