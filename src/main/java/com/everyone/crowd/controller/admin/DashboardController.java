package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.status.ProfileStatus;
import com.everyone.crowd.service.StatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;
import java.util.Date;

@Controller
public class DashboardController {

    private final StatService statService;

    public DashboardController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime();

        model.addAttribute("newDemandCount", statService.getDemandsCount(today));
        model.addAttribute("newOrderCount", statService.getOrdersCount(today));
        model.addAttribute("devCount", statService.getDevelopersCount(ProfileStatus.VERIFIED.name()));
        model.addAttribute("customerCount", statService.getCustomersCount(ProfileStatus.VERIFIED.name()));
        model.addAttribute("userCount", statService.getUsersCount());
        model.addAttribute("unRepliedFeedbackCount", statService.getFeedbacksCount());

        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        model.addAttribute("usedMemory", (totalMemory - freeMemory) / 1024 / 1024);
        model.addAttribute("freeMemory", freeMemory / 1024 / 1024);
        return "admin/dashboard";
    }
}
