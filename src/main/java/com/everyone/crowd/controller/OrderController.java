package com.everyone.crowd.controller;

import com.everyone.crowd.entity.*;
import com.everyone.crowd.entity.exception.NotAcceptableException;
import com.everyone.crowd.entity.exception.NotFoundException;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.entity.status.OrderStatus;
import com.everyone.crowd.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final BidService bidService;
    private final DevProfileService devProfileService;
    private final CustomerProfileService customerProfileService;
    private final DemandService demandService;

    public OrderController(OrderService orderService, BidService bidService, DevProfileService devProfileService,
                           CustomerProfileService customerProfileService, DemandService demandService) {
        this.orderService = orderService;
        this.bidService = bidService;
        this.devProfileService = devProfileService;
        this.customerProfileService = customerProfileService;
        this.demandService = demandService;
    }

    @GetMapping("/order/new/{bidId}")
    public String createOrder(Model model, HttpSession session, @PathVariable("bidId") Integer bidId) {
        User user = (User) session.getAttribute("user");
        Bid bid = bidService.findById(bidId);
        Demand demand = demandService.findById(bid.getDemandId());
        if (user.getId().equals(demand.getCustomerId()) && demand.getStatus().equals(DemandStatus.PASS.name())) {
            DevProfile devProfile = devProfileService.findById(bid.getDevId());
            model.addAttribute("bid", bid);
            model.addAttribute("demand", demand);
            model.addAttribute("devProfile", devProfile);
        } else {
            throw new NotAcceptableException("无法处理订单请求");
        }
        return "order-new";
    }

    @PostMapping("/order/new")
    public String confirmOrder(HttpSession session, Order order) {
        User user = (User) session.getAttribute("user");
        order.setCustomerId(user.getId());
        order.setOrderTime(new Date());
        order.setStatus(OrderStatus.UNPAID.name());
        orderService.place(order);
        demandService.updateStatus(order.getDemandId(), DemandStatus.CONTRACTED.name());
        return "redirect:/order/view/" + order.getId();
    }

    @GetMapping("/order/view/{id}")
    public String viewOrder(Model model, @PathVariable("id") Integer id) {
        Order order = orderService.findById(id);
        if (order == null) throw new NotFoundException("找不到请求的订单信息");
        model.addAttribute("order", order);
        model.addAttribute("devProfile", devProfileService.findById(order.getDevId()));
        model.addAttribute("customerProfile", customerProfileService.findById(order.getCustomerId()));
        model.addAttribute("demand", demandService.findById(order.getDemandId()));
        Map<String, String> orderStatusMap = new HashMap<>();
        orderStatusMap.put(OrderStatus.UNPAID.name(), "未付款");
        orderStatusMap.put(OrderStatus.PAID.name(), "进行中");
        orderStatusMap.put(OrderStatus.COMPLETED.name(), "已完成");
        model.addAttribute("orderStatusMap", orderStatusMap);
        return "order-view";
    }
}
