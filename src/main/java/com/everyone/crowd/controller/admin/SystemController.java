package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Message;
import com.everyone.crowd.service.SettingsService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class SystemController {

    private final SettingsService settingsService;

    public SystemController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("admin/system/settings")
    public String settingsPage(Model model) {
        model.addAttribute("settings", settingsService.getAll());
        return "admin/system-settings";
    }

    @PostMapping("admin/system/settings")
    public String settingsSave(HttpServletResponse response, @RequestParam Map<String, String> params) {
        settingsService.setAll(params);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "设置已经保存"), "/admin");
        return "redirect:/admin/system/settings";
    }
}
