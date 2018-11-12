package com.everyone.crowd.service;

import com.everyone.crowd.entity.User;

public interface UserService {

    User login(User userToLogin);

    User login(String cookie);

    User twoFactorAuth(User userToLogin, Integer twoFACode);

    void register(User user);

    void logout(User user);

    void update(User user);

    boolean usernameExists(String username);

    boolean activate(String activateCode);

    void updateEmail(User user);

    void updatePassword(User user);

    void updateTwoFactor(User user);

    void updateBalance(User user);
}
