package com.everyone.crowd.service;

import com.everyone.crowd.entity.Announcement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnouncementService {
    void insert(Announcement announcement);
    void delete(Integer id);
    void update(Announcement announcement);
    List<Announcement> findByTitle(String title);
}
