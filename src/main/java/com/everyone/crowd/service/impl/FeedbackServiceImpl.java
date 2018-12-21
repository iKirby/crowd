package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.FeedbackMapper;
import com.everyone.crowd.entity.Feedback;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackMapper feedbackMapper;

    @Autowired
    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public Feedback findById(Integer id) {
        return feedbackMapper.findById(id);
    }

    @Override
    public Page<Feedback> findAll(int pageSize, int page) {
        int total = feedbackMapper.count();
        List<Feedback> content = feedbackMapper.findAll(pageSize * (page - 1), pageSize);
        Page<Feedback> feedbackPage = new Page<>();
        feedbackPage.setContent(content);
        feedbackPage.setCurrentPage(page);
        feedbackPage.setTotal(total);
        feedbackPage.setPageSize(pageSize);
        return feedbackPage;
    }

    @Override
    public Page<Feedback> findNotReplied(int pageSize, int page) {
        int total = feedbackMapper.countNotReplied();
        List<Feedback> content = feedbackMapper.findNotReplied(pageSize * (page - 1), pageSize);
        Page<Feedback> feedbackPage = new Page<>();
        feedbackPage.setContent(content);
        feedbackPage.setCurrentPage(page);
        feedbackPage.setTotal(total);
        feedbackPage.setPageSize(pageSize);
        return feedbackPage;
    }

    @Override
    public Page<Feedback> findByUserId(Integer userId, int pageSize, int page) {
        int total = feedbackMapper.countByUserId(userId);
        List<Feedback> content = feedbackMapper.findByUserId(userId, pageSize * (page - 1), pageSize);
        Page<Feedback> feedbackPage = new Page<>();
        feedbackPage.setContent(content);
        feedbackPage.setCurrentPage(page);
        feedbackPage.setTotal(total);
        feedbackPage.setPageSize(pageSize);
        return feedbackPage;
    }

    @Override
    @Transactional
    public int submit(Feedback feedback) {
        return feedbackMapper.insert(feedback);
    }

    @Override
    @Transactional
    public void reply(Integer id, String reply) {
        feedbackMapper.reply(id, reply);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        feedbackMapper.delete(id);
    }
}
