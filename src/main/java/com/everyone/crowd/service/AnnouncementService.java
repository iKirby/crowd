package com.everyone.crowd.service;

import com.everyone.crowd.entity.Announcement;
import com.everyone.crowd.entity.Page;

import java.util.Date;

public interface AnnouncementService {
    void insert(Announcement announcement);

    void delete(Integer id);

    void update(Announcement announcement);

    Announcement findById(Integer id);

    Page<Announcement> findAll(int pageSize, int page);

    Page<Announcement> findAllTillNow(int pageSize, int page);
}
