package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.BidMapper;
import com.everyone.crowd.entity.Bid;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BidServiceImpl implements BidService {
    private final BidMapper bidMapper;

    @Autowired
    public BidServiceImpl(BidMapper bidMapper) {
        this.bidMapper = bidMapper;
    }

    @Override
    public Bid findById(Integer id) {
        return bidMapper.findById(id);
    }

    @Override
    public Page<Bid> findByDevId(Integer devId, int pageSize, int page) {
        int total = bidMapper.countByDevId(devId);
        List<Bid> content = bidMapper.findByDevId(devId, pageSize * (page - 1), pageSize);
        Page<Bid> bidPage = new Page<>();
        bidPage.setContent(content);
        bidPage.setCurrentPage(page);
        bidPage.setTotal(total);
        bidPage.setPageSize(pageSize);
        return bidPage;
    }

    @Override
    public Page<Bid> findByDemandId(Integer demandId, int pageSize, int page) {
        int total = bidMapper.countByDemandId(demandId);
        List<Bid> content = bidMapper.findByDemandId(demandId, pageSize * (page - 1), pageSize);
        Page<Bid> bidPage = new Page<>();
        bidPage.setContent(content);
        bidPage.setCurrentPage(page);
        bidPage.setTotal(total);
        bidPage.setPageSize(pageSize);
        return bidPage;
    }

    @Override
    @Transactional
    public int participate(Bid bid) {
        return bidMapper.insert(bid);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        bidMapper.delete(id);
    }
}
