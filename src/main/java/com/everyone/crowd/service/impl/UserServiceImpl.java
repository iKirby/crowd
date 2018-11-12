package com.everyone.crowd.service.impl;

import com.everyone.crowd.dao.UserMapper;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.MD5Util;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final GoogleAuthenticator ga = new GoogleAuthenticator();

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(User userToLogin) {
        User user = userMapper.findByUsername(userToLogin.getUsername());
        if (user != null) {
            if (MD5Util.verifySaltedString(user.getPassword(), userToLogin.getPassword())) {
                if (user.getTwoFactor() == null) {
                    String cookie = user.getUsername() + UUID.randomUUID().toString();
                    user.setCookie(cookie);
                }
                user.setPassword(null);
                user.setTwoFactor(null);
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
    public User twoFactorAuth(User userToLogin, Integer twoFACode) {
        User user = userMapper.findById(userToLogin.getId());
        if (ga.authorize(user.getTwoFactor(), twoFACode)) {
            user.setPassword(null);
            user.setTwoFactor("");
            return user;
        }
        return null;
    }

    @Override
    public void register(User user) {
        user.setActivateCode(MD5Util.encrypt(user.getUsername() + user.getEmail()));
        userMapper.insert(user);
    }

    @Override
    public void logout(User user) {
        userMapper.updateCookie(user.getId(), null);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public boolean usernameExists(String username) {
        return userMapper.countExists(username) > 0;
    }

    @Override
    public boolean activate(String activateCode) {
        return userMapper.activate(activateCode) > 0;
    }

    @Override
    public void updateEmail(User user) {
        userMapper.updateEmail(user.getId(), user.getEmail());
    }

    @Override
    public void updatePassword(User user) {
        userMapper.updatePassword(user.getId(), user.getPassword());
    }

    @Override
    public void updateTwoFactor(User user) {
        userMapper.updateTwoFactor(user.getId(), user.getTwoFactor());
    }

    @Override
    public void updateBalance(User user) {
        userMapper.updateBalance(user.getId(), user.getBalance());
    }
}
