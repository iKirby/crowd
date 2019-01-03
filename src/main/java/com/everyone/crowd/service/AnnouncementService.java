package com.everyone.crowd.service;

import com.everyone.crowd.entity.Announcement;
import com.everyone.crowd.entity.Page;

public interface AnnouncementService {
    void insert(Announcement announcement);

    void delete(Integer id);

    void update(Announcement announcement);

    Announcement findById(Integer id);

    Page<Announcement> findByTitle(String title, int pageSize, int page);

    Page<Announcement> findAll(int pageSize, int page);
}
