package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.StatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/admin/stat/demand")
    public String demandStat(Model model) {
        model.addAttribute("total", statService.getDemandsCount());
        model.addAttribute("pending", statService.getDemandsCount(DemandStatus.PENDING.name()));
        model.addAttribute("pass", statService.getDemandsCount(DemandStatus.PASS.name()));
        model.addAttribute("contracted", statService.getDemandsCount(DemandStatus.CONTRACTED.name()));
        model.addAttribute("fail", statService.getDemandsCount(DemandStatus.FAIL.name()));
        return "admin/stat-demand";
    }
}
