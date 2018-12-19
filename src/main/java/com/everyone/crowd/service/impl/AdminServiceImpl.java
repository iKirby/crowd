package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.AdminMapper;
import com.everyone.crowd.entity.Admin;
import com.everyone.crowd.service.AdminService;
import com.everyone.crowd.util.MD5Util;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
                if (admin.getTwoFactor() == null) {
                    String cookie = admin.getUsername() + UUID.randomUUID().toString();
                    adminMapper.updateCookie(admin.getId(), cookie);
                    admin.setCookie(cookie);
                } else {
                    admin.setTwoFactor("");
                }
                admin.setPassword(null);
                return admin;
            }
        }
        return null;
    }

    @Override
    public Admin twoFactorAuth(Admin adminToLogin, int twoFACode) {
        Admin admin = adminMapper.findById(adminToLogin.getId());
        if (ga.authorize(admin.getTwoFactor(), twoFACode)) {
            String cookie = admin.getUsername() + UUID.randomUUID().toString();
            adminMapper.updateCookie(admin.getId(), cookie);
            admin.setCookie(cookie);
            admin.setTwoFactor("");
            admin.setPassword(null);
            return admin;
        }
        return null;
    }

    @Override
    public Admin login(String cookie) {
        return adminMapper.findByCookie(cookie);
    }

    @Override
    public void logout(Admin admin) {
        adminMapper.updateCookie(admin.getId(), null);
    }

    @Override
    @Transactional
    public void updatePassword(Admin admin) {
        admin.setPassword(MD5Util.saltEncrypt(admin.getPassword()));
        adminMapper.updatePassword(admin.getId(), admin.getPassword());
    }

    @Override
    public Admin findById(Integer id) {
        Admin admin = adminMapper.findById(id);
        if (admin != null) {
            admin.setPassword(null);
            admin.setTwoFactor(admin.getTwoFactor() != null ? "" : null);
        }
        return admin;
    }

    @Override
    @Transactional
    public void updateEmail(Admin admin) {
        adminMapper.updateEmail(admin.getId(), admin.getEmail());
    }

    @Override
    @Transactional
    public void updateTwoFactor(Admin admin) {
        adminMapper.updateTwoFactor(admin.getId(), admin.getTwoFactor());
    }

    @Override
    public boolean checkPassword(Admin admin, String password) {
        return MD5Util.verifySaltedString(adminMapper.findById(admin.getId()).getPassword(), password);
    }

    @Override
    public boolean checkTwoFactor(Admin admin, int twoFACode) {
        return ga.authorize(adminMapper.findById(admin.getId()).getTwoFactor(), twoFACode);
    }
}
