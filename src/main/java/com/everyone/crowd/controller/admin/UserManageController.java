package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Message;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.MailService;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.PasswordGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;

@Controller
public class UserManageController {

    private final UserService userService;
    private final MailService mailService;
    private final PasswordGenerator passwordGenerator;

    public UserManageController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
        passwordGenerator = new PasswordGenerator.Builder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
    }

    @GetMapping("/admin/user")
    public String userList(Model model,
                           @RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<User> userPage;
        String cardTitle;
        if (keyword.isEmpty()) {
            userPage = userService.findAll(20, page);
            cardTitle = "全部用户";
        } else {
            userPage = userService.findByNameLike(keyword, 20, page);
            cardTitle = "搜索结果";
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("cardTitle", cardTitle);
        model.addAttribute("users", userPage);
        return "admin/user-manage";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String userEdit(Model model, @PathVariable("id") Integer id,
                           @RequestParam(value = "action", defaultValue = "") String action,
                           @RequestParam(value = "email", defaultValue = "") String email) throws MessagingException {
        User user = userService.findById(id);
        switch (action) {
            case "changeEmail":
                if (!email.isEmpty()) {
                    user.setEmail(email);
                    userService.updateEmail(user);
                    mailService.sendValidationEmail(user.getEmail(), user.getUsername(), user.getActivateCode());
                    model.addAttribute("message", new Message(Message.TYPE_SUCCESS, "邮箱已经修改，请提醒用户验证新的邮箱地址"));
                }
                break;
            case "resetPassword":
                String newPassword = passwordGenerator.generate(12);
                user.setPassword(newPassword);
                userService.updatePassword(user);
                user.setPassword("");
                mailService.sendResetPasswordEmail(user.getEmail(), user.getUsername(), newPassword);
                model.addAttribute("message", new Message(Message.TYPE_SUCCESS, "密码已经重置，请告知用户查收密码重置邮件"));
                break;
            case "disable2FA":
                user.setTwoFactor(null);
                userService.updateTwoFactor(user);
                model.addAttribute("message", new Message(Message.TYPE_SUCCESS, "两步验证已停用"));
                break;
        }

        model.addAttribute("user", user);
        return "admin/user-edit";
    }
}
