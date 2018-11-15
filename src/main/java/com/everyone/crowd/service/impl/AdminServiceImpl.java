package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.AdminMapper;
import com.everyone.crowd.entity.Admin;
import com.everyone.crowd.service.AdminService;
import com.everyone.crowd.util.MD5Util;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;
    private final GoogleAuthenticator ga = new GoogleAuthenticator();

    @Autowired
    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public Admin login(Admin adminToLogin) {
        Admin admin = adminMapper.findByUsername(adminToLogin.getUsername());
        if (admin != null) {
            if (MD5Util.verifySaltedString(admin.getPassword(), adminToLogin.getPassword())) {

                return admin;
            }
        }
        return null;
    }

    @Override
    public void logout(Admin admin) {
    }

    @Override
    @Transactional
    public void updatePassword(Admin admin) {
        adminMapper.updatePassword(admin.getId(), admin.getPassword());
    }

}
