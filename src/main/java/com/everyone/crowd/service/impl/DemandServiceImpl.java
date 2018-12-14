package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.DemandMapper;
import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DemandServiceImpl implements DemandService {

    private final DemandMapper demandMapper;

    @Autowired
    public DemandServiceImpl(DemandMapper demandMapper) {
        this.demandMapper = demandMapper;
    }

    @Override
    public Demand findById(Integer id) {
        return demandMapper.findById(id);
    }

    @Override
    public Page<Demand> findAll(int pageSize, int page) {
        int total = demandMapper.countAll();
        List<Demand> content = demandMapper.findAll(pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    public Page<Demand> findByTitle(String title, int pageSize, int page) {
        int total = demandMapper.countByTitle(title);
        List<Demand> content = demandMapper.findByTitle(title, pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    public Page<Demand> findByCustomerId(Integer customerId, int pageSize, int page) {
        int total = demandMapper.countByCustomerId(customerId);
        List<Demand> content = demandMapper.findByCustomerId(customerId, pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    public Page<Demand> findByCategoryId(Integer categoryId, int pageSize, int page) {
        int total = demandMapper.countByCategoryId(categoryId);
        List<Demand> content = demandMapper.findByCategoryId(categoryId, pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    public Page<Demand> findByStatus(String status, int pageSize, int page) {
        int total = demandMapper.countByStatus(status);
        List<Demand> content = demandMapper.findByStatus(status, pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    public Page<Demand> findByCategoryIdAndStatus(Integer categoryId, String status, int pageSize, int page) {
        int total = demandMapper.countByCategoryIdAndStatus(categoryId, status);
        List<Demand> content = demandMapper.findByCategoryIdAndStatus(categoryId, status, pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    public Page<Demand> findByCustomerIdAndCategoryId(Integer customerId, Integer categoryId, int pageSize, int page) {
        int total = demandMapper.countByCustomerIdAndCategoryId(customerId, categoryId);
        List<Demand> content = demandMapper.findByCustomerIdAndCategoryId(customerId, categoryId, pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }

    @Override
    @Transactional
    public int insert(Demand demand) {
        return demandMapper.insert(demand);
    }

    @Override
    @Transactional
    public void update(Demand demand) {
        demandMapper.update(demand);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, String status) {
        demandMapper.updateStatus(id, status);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        demandMapper.delete(id);
    }

    @Override
    public Map<String, String> getStatusMap() {
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(DemandStatus.PENDING.name(), "审核中");
        statusMap.put(DemandStatus.PASS.name(), "竞标中");
        statusMap.put(DemandStatus.FAIL.name(), "审核未通过");
        statusMap.put(DemandStatus.CONTRACTED.name(), "已被承包");
        return statusMap;
    }

    @Override
    public Page<Demand> findByMultipleConditions(String keyword, Integer categoryId, String region, BigDecimal lowPrice, BigDecimal highPrice,
                                                 Date startDateFrom, Date startDateTo, Date endDateFrom, Date endDateTo,
                                                 String status, int pageSize, int page) {
        int total = demandMapper.countByMultipleConditions(keyword, categoryId, region, lowPrice, highPrice,
                startDateFrom, startDateTo, endDateFrom, endDateTo, status);
        List<Demand> content = demandMapper.findByMultipleConditions(keyword, categoryId, region, lowPrice,
                highPrice, startDateFrom, startDateTo, endDateFrom, endDateTo, status, pageSize * (page - 1), pageSize);
        Page<Demand> demandPage = new Page<>();
        demandPage.setContent(content);
        demandPage.setCurrentPage(page);
        demandPage.setTotal(total);
        demandPage.setPageSize(pageSize);
        return demandPage;
    }
}
