package com.everyone.crowd.service;

import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.User;

public interface UserService {

    User login(User userToLogin);

    User login(String cookie);

    User twoFactorAuth(User userToLogin, Integer twoFACode);

    int register(User user);

    void logout(User user);

    void update(User user);

    boolean usernameExists(String username);

    boolean activate(String activateCode);

    void updateEmail(User user);

    boolean checkPassword(User user, String password);

    void updatePassword(User user);

    boolean checkTwoFactor(User user, Integer twoFACode);

    void updateTwoFactor(User user);

    void updateBalance(User user);

    Page<User> findAll(int pageSize, int page);

    Page<User> findByNameLike(String keyword, int pageSize, int page);

    User findById(Integer id);

    User findByUsernameAndEmail(String username, String email);
}
