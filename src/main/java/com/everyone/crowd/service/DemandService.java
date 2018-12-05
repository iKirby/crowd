package com.everyone.crowd.service;

import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.Page;

import java.math.BigDecimal;
import java.util.Date;

public interface DemandService {

    Demand findById(Integer id);

    Page<Demand> findAll(int pageSize, int page);

    Page<Demand> findByTitle(String title, int pageSize, int page);

    Page<Demand> findByCustomerId(Integer customerId, int pageSize, int page);

    Page<Demand> findByCategoryId(Integer categoryId, int pageSize, int page);

    Page<Demand> findByStatus(String status, int pageSize, int page);

    Page<Demand> findByCategoryIdAndStatus(Integer categoryId, String status, int pageSize, int page);

    Page<Demand> findByCustomerIdAndCategoryId(Integer customerId, Integer categoryId, int pageSize, int page);

    int insert(Demand demand);

    void update(Demand demand);

    void updateStatus(Integer id, String status);

    void delete(Integer id);

    Page<Demand> findByMultipleConditions(String keyword,
                                          Integer categoryId,
                                          String region,
                                          BigDecimal lowPrice,
                                          BigDecimal highPrice,
                                          Date startDateFrom,
                                          Date startDateTo,
                                          Date endDateFrom,
                                          Date endDateTo,
                                          String status,
                                          int pageSize,
                                          int page);
}
