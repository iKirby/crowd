package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.AnnouncementMapper;
import com.everyone.crowd.entity.Announcement;
import com.everyone.crowd.service.AnnouncementService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;
    private final GoogleAuthenticator ga = new GoogleAuthenticator();

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
    public void delete(Integer id) {
         announcementMapper.delete(id);
    }

    @Override
    @Transactional
    public void update(Announcement announcement) {
        announcementMapper.update(announcement);
    }

    @Override
    @Transactional
    public List<Announcement> findByTitle(String title) {

        return announcementMapper.findByTitle(title);
    }
}
