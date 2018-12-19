package com.everyone.crowd.service;

import com.everyone.crowd.entity.Admin;

public interface AdminService {
    Admin login(Admin adminToLogin);

    Admin login(String cookie);

    Admin twoFactorAuth(Admin adminToLogin, int twoFACode);

    void logout(Admin admin);

    void updatePassword(Admin admin);

    Admin findById(Integer id);

    void updateEmail(Admin admin);

    void updateTwoFactor(Admin admin);

    boolean checkPassword(Admin admin, String password);

    boolean checkTwoFactor(Admin admin, int twoFACode);
}
