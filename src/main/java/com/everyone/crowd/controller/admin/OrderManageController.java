package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Order;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderManageController {
    private final OrderService orderService;
    private final DemandService demandService;
    private final DevProfileService devProfileService;
    private final CustomerProfileService customerProfileService;


    public OrderManageController(OrderService orderService, DemandService demandService, DevProfileService devProfileService, CustomerProfileService customerProfileService) {
        this.orderService = orderService;
        this.demandService = demandService;
        this.devProfileService = devProfileService;
        this.customerProfileService = customerProfileService;


    }

    @GetMapping("/admin/order")
    public String orderList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Order> orderPage = orderService.findAll(20, page);
        model.addAttribute("orders", orderPage);
        model.addAttribute("demandMap", demandService.getIdNameMap());
        model.addAttribute("devProfileMap", devProfileService.getIdNameMap());
        model.addAttribute("customerProfileMap", customerProfileService.getIdNameMap());
        model.addAttribute("statusMap", orderService.getOrderStatusMap());
        return "admin/order-manage";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id) {
        orderService.delete(id);
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/view/{id}")
    public String ediBid(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("demandMap", demandService.getIdNameMap());
        model.addAttribute("devProfileMap", devProfileService.getIdNameMap());
        model.addAttribute("customerProfileMap", customerProfileService.getIdNameMap());
        model.addAttribute("statusMap",  orderService.getOrderStatusMap());
        return "admin/order-view";
    }
}
