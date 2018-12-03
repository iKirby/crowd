package com.everyone.crowd.service;

import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.VerifyRequest;

public interface VerifyRequestService {

    VerifyRequest findById(Integer id);

    Page<VerifyRequest> findByStatus(Boolean processed, int pageSize, int page);

    Page<VerifyRequest> findByUserId(Integer userId, int pageSize, int page);

    int request(VerifyRequest verifyRequest);

    void process(Integer id, boolean passed);

    void delete(Integer id);
}
