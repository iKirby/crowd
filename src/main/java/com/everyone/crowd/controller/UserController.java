package com.everyone.crowd.controller;

import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.Message;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.MailService;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.CookieUtil;
import com.everyone.crowd.util.PasswordGenerator;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
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
    private final DevProfileService devProfileService;
    private final CustomerProfileService customerProfileService;
    private final GoogleAuthenticator ga = new GoogleAuthenticator();

    @Autowired
    public UserController(UserService userService, DevProfileService devProfileService,
                          CustomerProfileService customerProfileService, MailService mailService) {
        this.userService = userService;
        this.devProfileService = devProfileService;
        this.customerProfileService = customerProfileService;
        this.mailService = mailService;
    }

    @GetMapping("/user/login")
    public String loginPage(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
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
                CookieUtil.addMessage(response, "user",
                        new Message(Message.TYPE_SUCCESS, "账户激活成功，现在您可以登录了"), "/");
            } else {
                CookieUtil.addMessage(response, "user",
                        new Message(Message.TYPE_DANGER, "账户激活失败，激活码无效"), "/");
            }
        }
        model.addAttribute("from", from);
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
                CookieUtil.addMessage(response, "user",
                        new Message(Message.TYPE_WARNING, "账户尚未激活，请先激活"), "/");
            } else if (userResult.getTwoFactor() != null) {
                session.setAttribute("userTo2FA", userResult);
                session.setAttribute("remember", remember);
                if (from.equals("/user/2fa")) {
                    return "redirect:/";
                }
                return "redirect:/user/2fa?from=" + from;
            } else {
                session.setAttribute("user", userResult);
                if (remember) {
                    addCookie(response, userResult);
                }
                CookieUtil.addMessage(response, "user",
                        new Message(Message.TYPE_DEFAULT, "欢迎，" + userResult.getUsername()), "/");
                return "redirect:" + from;
            }
        } else {
            CookieUtil.addMessage(response, "user",
                    new Message(Message.TYPE_DANGER, "用户名或密码错误"), "/");
        }
        user.setPassword("");
        model.addAttribute("from", from);
        model.addAttribute("userLogin", user);
        model.addAttribute("userRegister", new User());
        return "login";
    }

    @GetMapping("/user/2fa")
    public String twoFactorPage(Model model, HttpSession session,
                                @RequestParam(value = "from", defaultValue = "/") String from) {
        if (session.getAttribute("userTo2FA") == null) {
            return "redirect:" + from;
        } else {
            model.addAttribute("from", from);
            return "login-2fa";
        }
    }

    @PostMapping("/user/2fa")
    public String twoFactorLogin(Model model, HttpServletResponse response, HttpSession session,
                                 @RequestParam(value = "twoFactorCode") int twoFactorCode,
                                 @RequestParam(value = "from", defaultValue = "/") String from) {
        User userResult = userService.twoFactorAuth((User) session.getAttribute("userTo2FA"), twoFactorCode);
        if (userResult != null) {
            session.removeAttribute("userTo2FA");
            session.setAttribute("user", userResult);
            if ((boolean) session.getAttribute("remember")) {
                addCookie(response, userResult);
            }
            session.removeAttribute("remember");
            CookieUtil.addMessage(response, "user",
                    new Message(Message.TYPE_DEFAULT, "欢迎，" + userResult.getUsername()), "/");
            return "redirect:" + from;
        } else {
            model.addAttribute("from", from);
            CookieUtil.addMessage(response, "user",
                    new Message(Message.TYPE_DANGER, "两步验证验证码错误"), "/");
            return "login-2fa";
        }
    }

    @PostMapping("/user/resetPassword")
    public String resetPassword(HttpServletResponse response,
                                @RequestParam("username") String username,
                                @RequestParam("email") String email) throws MessagingException {
        User user = userService.findByUsernameAndEmail(username, email);
        if (user == null) {
            CookieUtil.addMessage(response, "user", new Message(Message.TYPE_WARNING, "没有找到对应的用户信息"), "/");
            return "redirect:/user/login?action=resetPassword";
        }
        PasswordGenerator passwordGenerator = new PasswordGenerator.Builder()
                .useUpper(true)
                .useLower(true)
                .useDigits(true)
                .build();
        String newPassword = passwordGenerator.generate(12);
        user.setPassword(newPassword);
        userService.updatePassword(user);
        mailService.sendResetPasswordEmail(user.getEmail(), user.getUsername(), newPassword);
        CookieUtil.addMessage(response, "user", new Message(Message.TYPE_INFO, "密码已经重置，请查收密码重置邮件"), "/");
        return "redirect:/user/login";
    }

    @PostMapping("/user/register")
    public String register(Model model, HttpServletResponse response, User user,
                           @RequestParam("confirmPassword") String confirmPassword) throws MessagingException {
        if (!userService.usernameExists(user.getUsername())) {
            if (confirmPassword.equals(user.getPassword())) {
                userService.register(user);
                mailService.sendActivateEmail(user.getEmail(), user.getUsername(), user.getActivateCode());
                model.addAttribute("result", "ok");
                CookieUtil.addMessage(response, "user",
                        new Message(Message.TYPE_SUCCESS, "注册成功，请查收账户激活邮件"), "/");
                user = new User();
            } else {
                CookieUtil.addMessage(response, "user",
                        new Message(Message.TYPE_WARNING, "密码和确认密码不匹配"), "/");
            }
        } else {
            CookieUtil.addMessage(response, "user",
                    new Message(Message.TYPE_WARNING, "用户名已被占用"), "/");
        }
        user.setPassword(null);
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
        CookieUtil.addMessage(response, "user",
                new Message(Message.TYPE_INFO, "已经退出登录"), "/");
        return "redirect:/";
    }

    @GetMapping("/user/profile")
    public String userCenter(Model model, HttpSession session,
                             @RequestParam(value = "page", defaultValue = "") String page) {
        User user = (User) session.getAttribute("user");
        generate2FAKey(model, user);
        model.addAttribute("page", page);
        DevProfile devProfile = devProfileService.findById(user.getId());
        CustomerProfile customerProfile = customerProfileService.findById(user.getId());
        if (devProfile == null) {
            devProfile = new DevProfile();
        }
        if (customerProfile == null) {
            customerProfile = new CustomerProfile();
        }
        model.addAttribute("devProfile", devProfile);
        model.addAttribute("customerProfile", customerProfile);
        return "usercenter";
    }

    @PostMapping("/user/profile")
    public String userCenter(Model model, HttpSession session, HttpServletResponse response,
                             @RequestParam(value = "page", defaultValue = "") String page,
                             @RequestParam(value = "action", defaultValue = "") String action,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "newPassword", required = false) String newPassword,
                             @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                             @RequestParam(value = "twoFactorKey", required = false) String twoFactorToken,
                             @RequestParam(value = "twoFactorCode", required = false) Integer twoFactorCode) throws MessagingException {
        User user = (User) session.getAttribute("user");

        switch (action) {
            case "changeEmail":
                user.setEmail(email);
                userService.updateEmail(user);
                mailService.sendValidationEmail(user.getEmail(), user.getUsername(), user.getActivateCode());
                CookieUtil.addMessage(response, "user",
                        new Message(Message.TYPE_INFO, "邮箱已经更改，请尽快查收验证邮件并完成验证"), "/");
                break;
            case "changePassword":
                if (userService.checkPassword(user, password)) {
                    if (newPassword.equals(confirmPassword)) {
                        user.setPassword(newPassword);
                        userService.updatePassword(user);
                        CookieUtil.addMessage(response, "user",
                                new Message(Message.TYPE_SUCCESS, "密码已经更改"), "/");
                        user.setPassword(null);
                    } else {
                        CookieUtil.addMessage(response, "user",
                                new Message(Message.TYPE_WARNING, "新密码和确认密码不匹配"), "/");
                    }
                } else {
                    CookieUtil.addMessage(response, "user",
                            new Message(Message.TYPE_WARNING, "原密码不正确"), "/");
                }
                break;
            case "enable2FA":
                if (ga.authorize(twoFactorToken, twoFactorCode)) {
                    user.setTwoFactor(twoFactorToken);
                    userService.updateTwoFactor(user);
                    user.setTwoFactor("");
                    CookieUtil.addMessage(response, "user",
                            new Message(Message.TYPE_SUCCESS, "两步验证已启用"), "/");
                } else {
                    CookieUtil.addMessage(response, "user",
                            new Message(Message.TYPE_WARNING, "两步验证验证码错误，无法启用"), "/");
                }
                break;
            case "disable2FA":
                if (userService.checkTwoFactor(user, twoFactorCode)) {
                    user.setTwoFactor(null);
                    userService.updateTwoFactor(user);
                    CookieUtil.addMessage(response, "user",
                            new Message(Message.TYPE_SUCCESS, "两步验证已停用"), "/");
                } else {
                    CookieUtil.addMessage(response, "user",
                            new Message(Message.TYPE_WARNING, "两步验证验证码错误，无法停用"), "/");
                }
                break;
        }

        generate2FAKey(model, user);
        session.setAttribute("user", user);
        model.addAttribute("page", page);
        DevProfile devProfile = devProfileService.findById(user.getId());
        CustomerProfile customerProfile = customerProfileService.findById(user.getId());
        if (devProfile == null) {
            devProfile = new DevProfile();
        }
        if (customerProfile == null) {
            customerProfile = new CustomerProfile();
        }
        model.addAttribute("devProfile", devProfile);
        model.addAttribute("customerProfile", customerProfile);
        return "usercenter";
    }

    @GetMapping("/user/validateEmail")
    public String validateEmail(Model model, @RequestParam("validateCode") String validateCode) {
        if (userService.activate(validateCode)) {
            model.addAttribute("message", "您的邮箱已经成功验证");
        } else {
            model.addAttribute("message", "邮箱验证失败，验证码无效");
        }
        return "validateemail";
    }

    private void generate2FAKey(Model model, User user) {
        if (user.getTwoFactor() == null) {
            GoogleAuthenticatorKey gaKey = ga.createCredentials();
            String totpURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("CROWDPlatform", user.getUsername(), gaKey);
            model.addAttribute("twoFactorKey", gaKey.getKey());
            model.addAttribute("twoFactorTotpURL", totpURL);
        }
    }

    private void addCookie(HttpServletResponse response, User userResult) {
        Cookie cookie = new Cookie("USR_LOGIN", userResult.getCookie());
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24 * 7);
        response.addCookie(cookie);
    }
}
