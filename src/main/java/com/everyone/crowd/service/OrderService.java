package com.everyone.crowd.service;

import com.everyone.crowd.entity.Order;
import com.everyone.crowd.entity.Page;

import java.util.Date;

public interface OrderService {

    Order findById(Integer id);

    Page<Order> findByDevId(Integer devId, int pageSize, int page);

    Page<Order> findByCustomerId(Integer customerId, int pageSize, int page);

    Page<Order> findByStatus(String status, int pageSize, int page);

    int place(Order order);

    void updateStatus(Integer id, String status);

    void completeOrder(Integer id, Date completeTime);

    void delete(Integer id);
}
