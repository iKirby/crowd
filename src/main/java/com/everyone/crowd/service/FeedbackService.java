package com.everyone.crowd.service;

import com.everyone.crowd.entity.Feedback;
import com.everyone.crowd.entity.Page;

public interface FeedbackService {

    Feedback findById(Integer id);

    Page<Feedback> findAll(int pageSize, int page);

    Page<Feedback> findNotReplied(int pageSize, int page);

    Page<Feedback> findByUserId(Integer userId, int pageSize, int page);

    int submit(Feedback feedback);

    void reply(Integer id, String reply);
}
