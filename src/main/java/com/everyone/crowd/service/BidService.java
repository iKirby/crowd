package com.everyone.crowd.service;

import com.everyone.crowd.entity.Bid;
import com.everyone.crowd.entity.Page;

public interface BidService {

    Bid findById(Integer id);

    Page<Bid> findByDevId(Integer devId, int pageSize, int page);

    Page<Bid> findByDemandId(Integer demandId, int pageSize, int page);

    int participate(Bid bid);

    void delete(Integer id);
}
