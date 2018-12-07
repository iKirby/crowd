package com.everyone.crowd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        model.addAttribute("usedMemory", (totalMemory - freeMemory) / 1024 / 1024);
        model.addAttribute("freeMemory", freeMemory / 1024 / 1024);
        return "admin/index";
    }
}
