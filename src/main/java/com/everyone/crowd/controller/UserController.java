package com.everyone.crowd.controller;

import com.everyone.crowd.configuration.Constants;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login")
    public String loginPage(Model model, HttpServletRequest request, HttpSession session,
                            @RequestParam(value = "action", defaultValue = "login") String action,
                            @RequestParam(value = "from", defaultValue = "/") String from) {
        String uCookie = CookieUtil.getCookieValue(Constants.COOKIE_USER_NAME, request.getCookies());
        if (!uCookie.isEmpty()) {
            User userResult = userService.login(uCookie);
            if (userResult != null) {
                session.setAttribute(Constants.SESSION_USER_NAME, userResult);
                return "redirect:" + from;
            }
        }
        model.addAttribute(Constants.MODEL_USER_NAME, new User());
        model.addAttribute(Constants.MODEL_USER_REGISTER_NAME, new User());
        model.addAttribute(Constants.MODEL_ACTION_NAME, action);
        return "login";
    }

    @PostMapping("/user/login")
    public String login(Model model, User user, HttpServletResponse response, HttpSession session,
                        @RequestParam(value = "from", defaultValue = "/") String from,
                        @RequestParam(value = "remember", defaultValue = "false") boolean remember) {
        User userResult = userService.login(user);
        if (userResult != null) {
            if (!userResult.isActivated()) {
                model.addAttribute(Constants.MODEL_MESSAGE_LOGIN_NAME, "您的账户尚未激活，请激活后再登录");
            } else if (userResult.getTwoFactor() != null) {
                session.setAttribute(Constants.SESSION_USER_2FA_NAME, userResult);
                return "redirect:/user/2fa?from=" + from;
            } else {
                session.setAttribute(Constants.SESSION_USER_NAME, user);
                if (remember) {
                    Cookie cookie = new Cookie(Constants.COOKIE_USER_NAME, userResult.getCookie());
                    cookie.setPath("/");
                    cookie.setMaxAge(3600 * 24 * 7);
                    response.addCookie(cookie);
                }
                return "redirect:" + from;
            }
        } else {
            model.addAttribute(Constants.MODEL_MESSAGE_LOGIN_NAME, "用户名或密码错误");
        }
        user.setPassword("");
        model.addAttribute(Constants.MODEL_USER_NAME, user);
        model.addAttribute(Constants.MODEL_USER_REGISTER_NAME, new User());
        return "login";
    }

    @PostMapping("/user/register")
    public String register(Model model, User user, @RequestParam("confirmPassword") String confirmPassword) {
        if (!userService.usernameExists(user.getUsername())) {
            if (confirmPassword.equals(user.getPassword())) {
                userService.register(user);
                model.addAttribute(Constants.MODEL_RESULT_NAME, "ok");
                model.addAttribute(Constants.MODEL_MESSAGE_REGISTER_NAME, "注册成功，请按照电子邮件中的说明激活账户");
                user = new User();
            } else {
                model.addAttribute(Constants.MODEL_MESSAGE_REGISTER_NAME, "密码和确认密码不匹配，请重新输入");
            }
        } else {
            model.addAttribute(Constants.MODEL_MESSAGE_REGISTER_NAME, "用户名已被占用，请更换后重试");
        }
        user.setPassword("");
        model.addAttribute(Constants.MODEL_USER_REGISTER_NAME, user);
        model.addAttribute(Constants.MODEL_ACTION_NAME, "register");
        return "login";
    }

    @GetMapping("/user/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        Cookie cookie = new Cookie(Constants.COOKIE_USER_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        session.removeAttribute(Constants.SESSION_USER_NAME);
        session.removeAttribute(Constants.SESSION_USER_2FA_NAME);
        return "redirect:/";
    }
}
