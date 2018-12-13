package com.everyone.crowd.controller;

import com.everyone.crowd.entity.Bid;
import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.BidService;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final BidService bidService;
    private final DevProfileService devProfileService;
    private final DemandService demandService;

    public OrderController(OrderService orderService, BidService bidService, DevProfileService devProfileService, DemandService demandService) {
        this.orderService = orderService;
        this.bidService = bidService;
        this.devProfileService = devProfileService;
        this.demandService = demandService;
    }

    @GetMapping("/order/new/{bidId}")
    public String createOrder(Model model, HttpSession session, @PathVariable("bidId") Integer bidId) {
        User user = (User) session.getAttribute("user");
        Bid bid = bidService.findById(bidId);
        Demand demand = demandService.findById(bid.getDemandId());
        if (user.getId().equals(bid.getDemandId()) && demand.getStatus().equals(DemandStatus.PASS.name())) {
            DevProfile devProfile = devProfileService.findById(bid.getDevId());
            model.addAttribute("bid", bid);
            model.addAttribute("demand", demand);
            model.addAttribute("devProfile", devProfile);
        } else {
            model.addAttribute("message", "无法创建新订单");
        }
        return "order-new";
    }
}
