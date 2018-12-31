package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Message;
import com.everyone.crowd.entity.Order;
import com.everyone.crowd.entity.OrderComment;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.OrderService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class OrderManageController {
    private final OrderService orderService;
    private final DemandService demandService;
    private final DevProfileService devProfileService;
    private final CustomerProfileService customerProfileService;


    public OrderManageController(OrderService orderService,
                                 DemandService demandService,
                                 DevProfileService devProfileService,
                                 CustomerProfileService customerProfileService) {
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
    public String deleteOrder(HttpServletResponse response, @PathVariable("id") Integer id) {
        orderService.delete(id);
        CookieUtil.addMessage(response, "admin", new Message(Message.TYPE_SUCCESS, "订单已经删除"), "/admin");
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/view/{id}")
    public String ediBid(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("demandMap", demandService.getIdNameMap());
        model.addAttribute("devProfileMap", devProfileService.getIdNameMap());
        model.addAttribute("customerProfileMap", customerProfileService.getIdNameMap());
        model.addAttribute("statusMap", orderService.getOrderStatusMap());
        OrderComment comment = orderService.findCommentByOrderId(id);
        model.addAttribute("comment", comment != null ? comment : new OrderComment());
        return "admin/order-view";
    }
}
