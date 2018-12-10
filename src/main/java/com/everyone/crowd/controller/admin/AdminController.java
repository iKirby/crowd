package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Admin;
import com.everyone.crowd.service.AdminService;
import com.everyone.crowd.util.CookieUtil;
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
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/login")
    public String loginPage(Model model, HttpServletRequest request, HttpSession session,
                            @RequestParam(value = "from", defaultValue = "/admin/dashboard") String from) {
        String aCookie = CookieUtil.getCookieValue("ADMIN_LOGIN", request.getCookies());
        if (!aCookie.isEmpty()) {
            Admin adminResult = adminService.login(aCookie);
            if (adminResult != null) {
                session.setAttribute("admin", adminResult);
                return "redirect:" + from;
            }
        }
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

    private void addCookie(HttpServletResponse response, Admin adminResult) {
        Cookie cookie = new Cookie("ADMIN_LOGIN", adminResult.getCookie());
        cookie.setPath("/admin");
        cookie.setMaxAge(3600 * 24 * 7);
        response.addCookie(cookie);
    }
}
