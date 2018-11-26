package com.everyone.crowd.controller;

import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.MailService;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/user/login")
    public String loginPage(Model model, HttpServletRequest request, HttpSession session,
                            @RequestParam(value = "action", defaultValue = "login") String action,
                            @RequestParam(value = "from", defaultValue = "/") String from,
                            @RequestParam(value = "activateCode", defaultValue = "") String activateCode) {
        String uCookie = CookieUtil.getCookieValue("USR_LOGIN", request.getCookies());
        if (!uCookie.isEmpty()) {
            User userResult = userService.login(uCookie);
            if (userResult != null) {
                session.setAttribute("user", userResult);
                return "redirect:" + from;
            }
        }
        if (action.equals("activate") && !activateCode.isEmpty()) {
            if (userService.activate(activateCode)) {
                model.addAttribute("result", "ok");
                model.addAttribute("messageLogin", "账户激活成功，现在您可以登录了");
            } else {
                model.addAttribute("messageLogin", "账户激活失败，激活码无效");
            }
        }
        model.addAttribute("userLogin", new User());
        model.addAttribute("userRegister", new User());
        model.addAttribute("action", action);
        return "login";
    }

    @PostMapping("/user/login")
    public String login(Model model, User user, HttpServletResponse response, HttpSession session,
                        @RequestParam(value = "from", defaultValue = "/") String from,
                        @RequestParam(value = "remember", defaultValue = "false") boolean remember) {
        User userResult = userService.login(user);
        if (userResult != null) {
            if (!userResult.isActivated()) {
                model.addAttribute("messageLogin", "您的账户尚未激活，请激活后再登录");
            } else if (userResult.getTwoFactor() != null) {
                session.setAttribute("userTo2FA", userResult);
                return "redirect:/user/2fa?from=" + from;
            } else {
                session.setAttribute("user", user);
                if (remember) {
                    Cookie cookie = new Cookie("USR_LOGIN", userResult.getCookie());
                    cookie.setPath("/");
                    cookie.setMaxAge(3600 * 24 * 7);
                    response.addCookie(cookie);
                }
                return "redirect:" + from;
            }
        } else {
            model.addAttribute("messageLogin", "用户名或密码错误");
        }
        user.setPassword("");
        model.addAttribute("userLogin", user);
        model.addAttribute("userRegister", new User());
        return "login";
    }

    @PostMapping("/user/register")
    public String register(Model model, User user, @RequestParam("confirmPassword") String confirmPassword) throws MessagingException {
        if (!userService.usernameExists(user.getUsername())) {
            if (confirmPassword.equals(user.getPassword())) {
                userService.register(user);
                mailService.sendActivateEmail(user.getEmail(), user.getUsername(), user.getActivateCode());
                model.addAttribute("result", "ok");
                model.addAttribute("messageRegister", "注册成功，请按照电子邮件中的说明激活账户");
                user = new User();
            } else {
                model.addAttribute("messageRegister", "密码和确认密码不匹配，请重新输入");
            }
        } else {
            model.addAttribute("messageRegister", "用户名已被占用，请更换后重试");
        }
        user.setPassword("");
        model.addAttribute("userRegister", user);
        model.addAttribute("userLogin", new User());
        model.addAttribute("action", "register");
        return "login";
    }

    @GetMapping("/user/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        Cookie cookie = new Cookie("USR_LOGIN", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        session.removeAttribute("user");
        session.removeAttribute("userTo2FA");
        return "redirect:/";
    }
}
