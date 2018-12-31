package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.OrderMapper;
import com.everyone.crowd.entity.Order;
import com.everyone.crowd.entity.OrderComment;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.status.OrderStatus;
import com.everyone.crowd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public Page<Order> findAll(int pageSize, int page) {
        int total = orderMapper.countAll();
        List<Order> content = orderMapper.findAll(pageSize * (page - 1), pageSize);
        Page<Order> orderPage = new Page<>();
        orderPage.setContent(content);
        orderPage.setCurrentPage(page);
        orderPage.setTotal(total);
        orderPage.setPageSize(pageSize);
        return orderPage;
    }

    @Override
    public Order findById(Integer id) {
        return orderMapper.findById(id);
    }

    @Override
    public Page<Order> findByDevId(Integer devId, int pageSize, int page) {
        int total = orderMapper.countByDevId(devId);
        List<Order> content = orderMapper.findByDevId(devId, pageSize * (page - 1), pageSize);
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
        List<Order> content = orderMapper.findByCustomerId(customerId, pageSize * (page - 1), pageSize);
        Page<Order> orderPage = new Page<>();
        orderPage.setContent(content);
        orderPage.setCurrentPage(page);
        orderPage.setTotal(total);
        orderPage.setPageSize(pageSize);
        return orderPage;
    }

    @Override
    public Page<Order> findByDevIdAndStatus(Integer devId, String status, int pageSize, int page) {
        int total = orderMapper.countByDevIdAndStatus(devId, status);
        List<Order> content = orderMapper.findByDevIdAndStatus(devId, status, pageSize * (page - 1), pageSize);
        Page<Order> orderPage = new Page<>();
        orderPage.setContent(content);
        orderPage.setCurrentPage(page);
        orderPage.setTotal(total);
        orderPage.setPageSize(pageSize);
        return orderPage;
    }

    @Override
    public Page<Order> findByCustomerIdAndStatus(Integer customerId, String status, int pageSize, int page) {
        int total = orderMapper.countByCustomerIdAndStatus(customerId, status);
        List<Order> content = orderMapper.findByCustomerIdAndStatus(customerId, status, pageSize * (page - 1), pageSize);
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
        List<Order> content = orderMapper.findByStatus(status, pageSize * (page - 1), pageSize);
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
        orderMapper.updateStatus(id, OrderStatus.COMPLETED.name());
        orderMapper.completeOrder(id, completeTime);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        orderMapper.delete(id);
    }

    @Override
    public OrderComment findCommentByOrderId(Integer orderId) {
        return orderMapper.findCommentByOrderId(orderId);
    }

    @Override
    @Transactional
    public void devComment(Integer orderId, String comment) {
        if (orderMapper.commentExists(orderId) > 0) {
            orderMapper.updateDevComment(orderId, comment);
        } else {
            OrderComment orderComment = new OrderComment();
            orderComment.setOrderId(orderId);
            orderComment.setDevComment(comment);
            orderMapper.insertDevComment(orderComment);
        }
    }

    @Override
    @Transactional
    public void customerComment(Integer orderId, String comment) {
        if (orderMapper.commentExists(orderId) > 0) {
            orderMapper.updateCustomerComment(orderId, comment);
        } else {
            OrderComment orderComment = new OrderComment();
            orderComment.setOrderId(orderId);
            orderComment.setCustomerComment(comment);
            orderMapper.insertCustomerComment(orderComment);
        }
    }

    @Override
    public Map<String, String> getOrderStatusMap() {
        Map<String, String> orderStatusMap = new HashMap<>();
        orderStatusMap.put(OrderStatus.UNPAID.name(), "未付款");
        orderStatusMap.put(OrderStatus.PAID.name(), "进行中");
        orderStatusMap.put(OrderStatus.PENDING.name(), "等待验收");
        orderStatusMap.put(OrderStatus.COMPLETED.name(), "已完成");
        return orderStatusMap;
    }
}
