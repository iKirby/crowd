package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.VerifyRequestMapper;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.VerifyRequest;
import com.everyone.crowd.service.VerifyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerifyRequestServiceImpl implements VerifyRequestService {
    private final VerifyRequestMapper verifyRequestMapper;

    @Autowired
    public VerifyRequestServiceImpl(VerifyRequestMapper verifyRequestMapper) {
        this.verifyRequestMapper = verifyRequestMapper;
    }

    @Override
    public VerifyRequest findById(Integer id) {
        return verifyRequestMapper.findById(id);
    }

    @Override
    public Page<VerifyRequest> findByStatus(Boolean processed, int pageSize, int page) {
        int total = verifyRequestMapper.countByStatus(processed);
        List<VerifyRequest> content = verifyRequestMapper.findByStatus(processed, pageSize * (page - 1), pageSize);
        Page<VerifyRequest> verifyRequestPage = new Page<>();
        verifyRequestPage.setContent(content);
        verifyRequestPage.setCurrentPage(page);
        verifyRequestPage.setTotal(total);
        verifyRequestPage.setPageSize(pageSize);
        return verifyRequestPage;
    }

    @Override
    public Page<VerifyRequest> findByUserId(Integer userId, int pageSize, int page) {
        int total = verifyRequestMapper.countByUserId(userId);
        List<VerifyRequest> content = verifyRequestMapper.findByUserId(userId, pageSize * (page - 1), pageSize);
        Page<VerifyRequest> verifyRequestPage = new Page<>();
        verifyRequestPage.setContent(content);
        verifyRequestPage.setCurrentPage(page);
        verifyRequestPage.setTotal(total);
        verifyRequestPage.setPageSize(pageSize);
        return verifyRequestPage;
    }

    @Override
    public int request(VerifyRequest verifyRequest) {
        return verifyRequestMapper.insert(verifyRequest);
    }

    @Override
    public void process(Integer id) {
        // TODO Add profile status modification
        verifyRequestMapper.process(id);
    }

    @Override
    public void delete(Integer id) {
        verifyRequestMapper.deleteById(id);
    }
}
