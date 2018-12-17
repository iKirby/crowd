package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserManageController {

    private final UserService userService;

    public UserManageController(UserService userService) {
        this.userService = userService;
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
    public String userEdit(Model model, @PathVariable("id") Integer userId,
                           @RequestParam(value = "action", required = false) String action) {
        model.addAttribute("user", userService.findById(userId));
        return "admin/user-edit";
    }
}
