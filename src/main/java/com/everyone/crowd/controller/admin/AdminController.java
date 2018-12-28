package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Admin;
import com.everyone.crowd.entity.Message;
import com.everyone.crowd.service.AdminService;
import com.everyone.crowd.util.CookieUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final GoogleAuthenticator ga = new GoogleAuthenticator();

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String admin() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/login")
    public String loginPage(Model model, @RequestParam(value = "from", defaultValue = "/admin/dashboard") String from) {
        model.addAttribute("from", from);
        model.addAttribute("adminLogin", new Admin());
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String login(Model model, Admin admin, HttpServletResponse response, HttpSession session,
                        @RequestParam(value = "from", defaultValue = "/admin/dashboard") String from,
                        @RequestParam(value = "remember", defaultValue = "false") boolean remember) {
        Admin adminResult = adminService.login(admin);
        if (adminResult != null) {
            if (adminResult.getTwoFactor() != null) {
                session.setAttribute("adminTo2FA", adminResult);
                session.setAttribute("rememberAdmin", remember);
                if (from.equals("/admin/2fa")) {
                    return "redirect:/admin/dashboard";
                }
                return "redirect:/admin/2fa?from=" + from;
            } else {
                session.setAttribute("admin", adminResult);
                if (remember) {
                    addCookie(response, adminResult);
                }
                return "redirect:" + from;
            }
        }
        admin.setPassword("");
        model.addAttribute("message", "用户名或密码错误");
        model.addAttribute("adminLogin", admin);
        return "admin/login";
    }

    @GetMapping("/admin/2fa")
    public String twoFactorPage(Model model, HttpSession session,
                                @RequestParam(value = "from", defaultValue = "/admin") String from) {
        if (session.getAttribute("adminTo2FA") == null) {
            return "redirect:" + from;
        } else {
            model.addAttribute("from", from);
            return "admin/login-2fa";
        }
    }

    @PostMapping("/admin/2fa")
    public String twoFactorLogin(Model model, HttpServletResponse response, HttpSession session,
                                 @RequestParam(value = "twoFactorCode") int twoFactorCode,
                                 @RequestParam(value = "from", defaultValue = "/") String from) {
        Admin adminResult = adminService.twoFactorAuth((Admin) session.getAttribute("adminTo2FA"), twoFactorCode);
        if (adminResult != null) {
            session.removeAttribute("adminTo2FA");
            session.setAttribute("admin", adminResult);
            if ((boolean) session.getAttribute("rememberAdmin")) {
                addCookie(response, adminResult);
            }
            session.removeAttribute("rememberAdmin");
            CookieUtil.addMessage(response, "admin",
                    new Message(Message.TYPE_DEFAULT, "欢迎，" + adminResult.getUsername()), "/admin");
            return "redirect:" + from;
        } else {
            model.addAttribute("from", from);
            model.addAttribute("message", "两步验证验证码错误");
            return "admin/login-2fa";
        }
    }

    @GetMapping("/admin/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        session.removeAttribute("admin");
        session.removeAttribute("adminTo2FA");
        Cookie cookie = new Cookie("ADMIN_LOGIN", "");
        cookie.setPath("/admin");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/profile")
    public String profilePage(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        generate2FAKey(model, admin);
        return "admin/my-profile";
    }

    @PostMapping("/admin/profile")
    public String editProfile(HttpSession session, HttpServletResponse response,
                              @RequestParam(value = "action", defaultValue = "") String action,
                              @RequestParam(value = "password", required = false) String password,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "newPassword", required = false) String newPassword,
                              @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                              @RequestParam(value = "twoFactorKey", required = false) String twoFactorKey,
                              @RequestParam(value = "twoFactorCode", required = false) Integer twoFactorCode) {
        Admin admin = (Admin) session.getAttribute("admin");
        switch (action) {
            case "changeEmail":
                admin.setEmail(email);
                adminService.updateEmail(admin);
                CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_SUCCESS, "邮箱已经更改"), "/admin");
                break;
            case "changePassword":
                if (adminService.checkPassword(admin, password)) {
                    if (newPassword.equals(confirmPassword)) {
                        admin.setPassword(newPassword);
                        adminService.updatePassword(admin);
                        CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_SUCCESS, "密码已经更改"), "/admin");
                    } else {
                        CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_WARNING, "新密码和确认密码不匹配"), "/admin");
                    }
                } else {
                    CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_WARNING, "当前密码不正确"), "/admin");
                }
                break;
            case "enable2FA":
                if (ga.authorize(twoFactorKey, twoFactorCode)) {
                    admin.setTwoFactor(twoFactorKey);
                    adminService.updateTwoFactor(admin);
                    CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_SUCCESS, "两步验证已启用"), "/admin");
                } else {
                    CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_WARNING, "两步验证验证码错误"), "/admin");
                }
                break;
            case "disable2FA":
                if (adminService.checkTwoFactor(admin, twoFactorCode)) {
                    admin.setTwoFactor(null);
                    adminService.updateTwoFactor(admin);
                    CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_SUCCESS, "两步验证已停用"), "/admin");
                } else {
                    CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_WARNING, "两步验证验证码错误"), "/admin");
                }
                break;
        }
        return "redirect:/admin/profile";
    }

    private void generate2FAKey(Model model, Admin admin) {
        if (admin.getTwoFactor() == null) {
            GoogleAuthenticatorKey gaKey = ga.createCredentials();
            String totpURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("CROWDPlatform-Admin", admin.getUsername(), gaKey);
            model.addAttribute("twoFactorKey", gaKey.getKey());
            model.addAttribute("twoFactorTotpURL", totpURL);
        }
    }

    private void addCookie(HttpServletResponse response, Admin adminResult) {
        Cookie cookie = new Cookie("ADMIN_LOGIN", adminResult.getCookie());
        cookie.setPath("/admin");
        cookie.setMaxAge(3600 * 24 * 7);
        response.addCookie(cookie);
    }
}
