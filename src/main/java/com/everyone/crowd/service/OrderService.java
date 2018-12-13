package com.everyone.crowd.service;

import com.everyone.crowd.entity.Order;
import com.everyone.crowd.entity.OrderComment;
import com.everyone.crowd.entity.Page;

import java.util.Date;
import java.util.Map;

public interface OrderService {

    Order findById(Integer id);

    Page<Order> findByDevId(Integer devId, int pageSize, int page);

    Page<Order> findByCustomerId(Integer customerId, int pageSize, int page);

    Page<Order> findByDevIdAndStatus(Integer customerId, String status, int pageSize, int page);

    Page<Order> findByCustomerIdAndStatus(Integer customerId, String status, int pageSize, int page);

    Page<Order> findByStatus(String status, int pageSize, int page);

    int place(Order order);

    void updateStatus(Integer id, String status);

    void completeOrder(Integer id, Date completeTime);

    void delete(Integer id);

    OrderComment findCommentByOrderId(Integer orderId);

    void devComment(Integer orderId, String comment);

    void customerComment(Integer orderId, String comment);

    Map<String, String> getOrderStatusMap();
}
