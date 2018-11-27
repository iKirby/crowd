package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.DemandMapper;
import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    @Transactional
    public int insert(Demand demand) {
        demand.setPublishTime(new Date());
        return demandMapper.insert(demand);
    }

    @Override
    @Transactional
    public void update(Integer id, Demand demand) {
        demand.setPublishTime(new Date());
        demandMapper.update(id, demand);
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
}
