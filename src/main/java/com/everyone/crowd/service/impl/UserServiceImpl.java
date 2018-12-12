package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.UserMapper;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.MD5Util;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    // GoogleAuthenticator is for 2-factor authentication on login
    private final GoogleAuthenticator ga = new GoogleAuthenticator();

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User login(User userToLogin) {
        User user = userMapper.findByUsername(userToLogin.getUsername());
        if (user != null) {
            if (MD5Util.verifySaltedString(user.getPassword(), userToLogin.getPassword())) {
                if (user.getTwoFactor() == null) {
                    String cookie = user.getUsername() + UUID.randomUUID().toString();
                    userMapper.updateCookie(user.getId(), cookie);
                    user.setCookie(cookie);
                } else {
                    user.setTwoFactor("");
                }
                user.setPassword(null);
                return user;
            }
        }
        return null;
    }

    @Override
    public User login(String cookie) {
        return userMapper.findByCookie(cookie);
    }

    @Override
    @Transactional
    public User twoFactorAuth(User userToLogin, Integer twoFACode) {
        User user = userMapper.findById(userToLogin.getId());
        if (ga.authorize(user.getTwoFactor(), twoFACode)) {
            String cookie = user.getUsername() + UUID.randomUUID().toString();
            userMapper.updateCookie(user.getId(), cookie);
            user.setCookie(cookie);
            user.setPassword(null);
            user.setTwoFactor("");
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public int register(User user) {
        user.setPassword(MD5Util.saltEncrypt(user.getPassword()));
        user.setActivateCode(MD5Util.encrypt(user.getUsername()) + MD5Util.encrypt(user.getEmail()));
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public void logout(User user) {
        userMapper.updateCookie(user.getId(), null);
    }

    @Override
    @Transactional
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public boolean usernameExists(String username) {
        return userMapper.countExists(username) > 0;
    }

    @Override
    @Transactional
    public boolean activate(String activateCode) {
        if (!activateCode.isEmpty()) {
            return userMapper.activate(activateCode) > 0;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void updateEmail(User user) {
        user.setActivateCode(MD5Util.encrypt(user.getUsername()) + MD5Util.encrypt(user.getEmail()));
        userMapper.updateEmail(user.getId(), user.getEmail(), user.getActivateCode());
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return MD5Util.verifySaltedString(userMapper.findById(user.getId()).getPassword(), password);
    }

    @Override
    @Transactional
    public void updatePassword(User user) {
        user.setPassword(MD5Util.saltEncrypt(user.getPassword()));
        userMapper.updatePassword(user.getId(), user.getPassword());
    }

    @Override
    public boolean checkTwoFactor(User user, Integer twoFACode) {
        return ga.authorize(userMapper.findById(user.getId()).getTwoFactor(), twoFACode);
    }

    @Override
    @Transactional
    public void updateTwoFactor(User user) {
        userMapper.updateTwoFactor(user.getId(), user.getTwoFactor());
    }

    @Override
    @Transactional
    public void updateBalance(User user) {
        userMapper.updateBalance(user.getId(), user.getBalance());
    }

    @Override
    public Page<User> findAllPaged(int pageSize, int page) {
        int total = userMapper.getUserCount();
        List<User> content = userMapper.findAllPaged(pageSize * (page - 1), pageSize);
        Page<User> userPage = new Page<>();
        userPage.setContent(content);
        userPage.setCurrentPage(page);
        userPage.setTotal(total);
        userPage.setPageSize(pageSize);
        return userPage;
    }

    @Override
    public User findById(Integer id) {
        User user = userMapper.findById(id);
        if (user != null) {
            user.setPassword("");
            user.setTwoFactor(user.getTwoFactor() != null ? "" : null);
        }
        return user;
    }
}
