package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.AnnouncementMapper;
import com.everyone.crowd.entity.Announcement;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "announcement")
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementMapper announcementMapper) {
        this.announcementMapper = announcementMapper;
    }

    @Override
    @Transactional
    public void insert(Announcement announcement) {
        announcementMapper.insert(announcement);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#p0")
    public void delete(Integer id) {
        announcementMapper.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#p0.id")
    public void update(Announcement announcement) {
        announcementMapper.update(announcement);
    }

    @Override
    @Cacheable(key = "#p0")
    public Announcement findById(Integer id) {
        return announcementMapper.findById(id);
    }

    @Override
    public Page<Announcement> findByTitle(String title, int pageSize, int page) {
        int total = announcementMapper.countByTitle(title);
        List<Announcement> content = announcementMapper.findByTitle(title, pageSize * (page - 1), pageSize);
        Page<Announcement> announcementPage = new Page<>();
        announcementPage.setContent(content);
        announcementPage.setCurrentPage(page);
        announcementPage.setTotal(total);
        announcementPage.setPageSize(pageSize);
        return announcementPage;
    }

    @Override
    public Page<Announcement> findAll(int pageSize, int page) {
        int total = announcementMapper.count();
        List<Announcement> content = announcementMapper.findAll(pageSize * (page - 1), pageSize);
        Page<Announcement> announcementPage = new Page<>();
        announcementPage.setContent(content);
        announcementPage.setCurrentPage(page);
        announcementPage.setTotal(total);
        announcementPage.setPageSize(pageSize);
        return announcementPage;
    }
}
