package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Bid;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.BidService;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.service.DevProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BidManageController {
    private final BidService bidService;
    private final DemandService demandService;
    private final DevProfileService devProfileService;

    public BidManageController(BidService bidService, DemandService demandService, DevProfileService devProfileService) {
        this.bidService = bidService;
        this.demandService = demandService;
        this.devProfileService = devProfileService;

    }

    @GetMapping("/admin/bid")
    public String BidList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Bid> bidPage = bidService.findAll(20, page);
        model.addAttribute("bids", bidPage);
        model.addAttribute("demandMap", demandService.getIdNameMap());
        model.addAttribute("devProfileMap", devProfileService.getIdNameMap());
        return "admin/bid-manage";
    }

    @GetMapping("/admin/bid/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidService.delete(id);
        return "redirect:/admin/bid";
    }

    @GetMapping("/admin/bid/view/{id}")
    public String ediBid(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("bid", bidService.findById(id));
        model.addAttribute("demandMap", demandService.getIdNameMap());
        model.addAttribute("devProfileMap", devProfileService.getIdNameMap());
        return "admin/bid-view";
    }
}
