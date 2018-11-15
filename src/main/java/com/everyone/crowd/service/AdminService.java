package com.everyone.crowd.service;

import com.everyone.crowd.entity.Admin;

public interface AdminService {
    Admin login(Admin adminToLogin);

    Admin twoFactorAuth(Admin adminToLogin, int twoFACode);

    void logout(Admin admin);

    void updatePassword(Admin admin);
}
