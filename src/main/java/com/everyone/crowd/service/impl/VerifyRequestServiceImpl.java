package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.CustomerProfileMapper;
import com.everyone.crowd.dao.DevProfileMapper;
import com.everyone.crowd.dao.UserMapper;
import com.everyone.crowd.dao.VerifyRequestMapper;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.VerifyRequest;
import com.everyone.crowd.entity.status.ProfileStatus;
import com.everyone.crowd.entity.status.UserType;
import com.everyone.crowd.service.VerifyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VerifyRequestServiceImpl implements VerifyRequestService {
    private final VerifyRequestMapper verifyRequestMapper;
    private final DevProfileMapper devProfileMapper;
    private final CustomerProfileMapper customerProfileMapper;
    private final UserMapper userMapper;

    @Autowired
    public VerifyRequestServiceImpl(VerifyRequestMapper verifyRequestMapper,
                                    DevProfileMapper devProfileMapper,
                                    CustomerProfileMapper customerProfileMapper,
                                    UserMapper userMapper) {
        this.verifyRequestMapper = verifyRequestMapper;
        this.devProfileMapper = devProfileMapper;
        this.customerProfileMapper = customerProfileMapper;
        this.userMapper = userMapper;
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
    @Transactional
    public int request(VerifyRequest verifyRequest) {
        return verifyRequestMapper.insert(verifyRequest);
    }

    @Override
    @Transactional
    public void process(Integer id, boolean passed) {
        VerifyRequest request = verifyRequestMapper.findById(id);
        String status = passed ? ProfileStatus.VERIFIED.name() : ProfileStatus.FAILED.name();
        if (request.getType().equals(UserType.DEVELOPER.name())) {
            devProfileMapper.updateStatus(request.getUserId(), status);
            devProfileMapper.updateCert(request.getUserId(), request.getRealName());
            if (passed) userMapper.setDeveloper(request.getUserId(), true);
        } else {
            customerProfileMapper.updateStatus(request.getUserId(), status);
            customerProfileMapper.updateCert(request.getUserId(), request.getRealName());
            if (passed) userMapper.setCustomer(request.getUserId(), true);
        }
        verifyRequestMapper.process(id);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        verifyRequestMapper.deleteById(id);
    }
}
