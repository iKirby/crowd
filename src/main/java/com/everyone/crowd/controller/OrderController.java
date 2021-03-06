package com.everyone.crowd.controller;

import com.everyone.crowd.entity.*;
import com.everyone.crowd.entity.exception.ForbiddenException;
import com.everyone.crowd.entity.exception.NotAcceptableException;
import com.everyone.crowd.entity.exception.NotFoundException;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.entity.status.OrderStatus;
import com.everyone.crowd.service.*;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        if (bid == null) throw new NotFoundException("找不到请求的竞标信息");
        Demand demand = demandService.findById(bid.getDemandId());
        if (user.getId().equals(demand.getCustomerId()) && demand.getStatus().equals(DemandStatus.PASS.name())) {
            DevProfile devProfile = devProfileService.findById(bid.getDevId());
            model.addAttribute("bid", bid);
            model.addAttribute("demand", demand);
            model.addAttribute("devProfile", devProfile);
        } else {
            throw new NotAcceptableException("请求非法，无法创建订单");
        }
        return "order-new";
    }

    @PostMapping("/order/new")
    public String confirmOrder(HttpServletResponse response, HttpSession session, Order order) {
        User user = (User) session.getAttribute("user");
        order.setCustomerId(user.getId());
        order.setOrderTime(new Date());
        order.setStatus(OrderStatus.UNPAID.name());
        orderService.place(order);
        demandService.updateStatus(order.getDemandId(), DemandStatus.CONTRACTED.name());
        CookieUtil.addMessage(response, "user",
                new Message(Message.TYPE_SUCCESS, "订单已经创建"), "/");
        return "redirect:/order/view/" + order.getId();
    }

    @GetMapping("/order/my/dev")
    public String myDevOrderList(Model model, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "status", required = false) String status) {
        User user = (User) session.getAttribute("user");
        Page<Order> orderPage;
        if (status != null && !status.isEmpty()) {
            orderPage = orderService.findByDevIdAndStatus(user.getId(), status, 10, page);
        } else {
            orderPage = orderService.findByDevId(user.getId(), 10, page);
        }
        model.addAttribute("orders", orderPage);
        model.addAttribute("orderStatusMap", orderService.getOrderStatusMap());
        model.addAttribute("title", "开发者订单");
        model.addAttribute("status", status);
        if (orderPage.getTotal() > 0) {
            List<Integer> customerIds = new ArrayList<>();
            for (Order order : orderPage.getContent()) {
                customerIds.add(order.getCustomerId());
            }
            model.addAttribute("customerIdNameMap", customerProfileService.getIdNameMap(customerIds));
        }
        return "order-my";
    }

    @GetMapping("/order/my/customer")
    public String myCustomerOrderList(Model model, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "status", required = false) String status) {
        User user = (User) session.getAttribute("user");
        Page<Order> orderPage;
        if (status != null && !status.isEmpty()) {
            orderPage = orderService.findByCustomerIdAndStatus(user.getId(), status, 10, page);
        } else {
            orderPage = orderService.findByCustomerId(user.getId(), 10, page);
        }
        model.addAttribute("orders", orderPage);
        model.addAttribute("orderStatusMap", orderService.getOrderStatusMap());
        model.addAttribute("title", "需求方订单");
        model.addAttribute("status", status);
        if (orderPage.getTotal() > 0) {
            List<Integer> devIds = new ArrayList<>();
            for (Order order : orderPage.getContent()) {
                devIds.add(order.getDevId());
            }
            model.addAttribute("devIdNameMap", devProfileService.getIdNameMap(devIds));
        }
        return "order-my";
    }

    @GetMapping("/order/view/{id}")
    public String viewOrder(Model model, HttpSession session, @PathVariable("id") Integer id) {
        Order order = orderService.findById(id);
        if (order == null) throw new NotFoundException("找不到请求的订单信息");
        User user = (User) session.getAttribute("user");
        boolean isDev = user.getId().equals(order.getDevId());
        boolean isCustomer = user.getId().equals(order.getCustomerId());
        if (isDev || isCustomer) {
            model.addAttribute("order", order);
            model.addAttribute("devProfile", devProfileService.findById(order.getDevId()));
            model.addAttribute("customerProfile", customerProfileService.findById(order.getCustomerId()));
            model.addAttribute("demand", demandService.findById(order.getDemandId()));
            model.addAttribute("orderStatusMap", orderService.getOrderStatusMap());
            model.addAttribute("isCustomer", isCustomer);
            model.addAttribute("isDev", isDev);
            OrderComment comment = orderService.findCommentByOrderId(id);
            model.addAttribute("comment", comment != null ? comment : new OrderComment());
            return "order-view";
        } else {
            throw new ForbiddenException("当前账户无法查看此订单");
        }
    }

    @GetMapping("/order/delete/{id}")
    public String deleteOrder(HttpServletResponse response, HttpSession session, @PathVariable("id") Integer id) {
        Order order = orderService.findById(id);
        if (order == null) throw new NotFoundException("找不到请求的订单信息");
        User user = (User) session.getAttribute("user");
        if (user.getId().equals(order.getCustomerId()) && order.getStatus().equals(OrderStatus.UNPAID.name())) {
            orderService.delete(id);
            demandService.updateStatus(order.getDemandId(), DemandStatus.PASS.name());
            CookieUtil.addMessage(response, "user",
                    new Message(Message.TYPE_SUCCESS, "订单已经删除"), "/");
            return "redirect:/order/my/customer";
        } else {
            throw new NotAcceptableException("请求非法，无法删除订单");
        }
    }

    @GetMapping("/order/pay/{id}")
    public String payOrder(HttpServletResponse response, HttpSession session, @PathVariable("id") Integer id) {
        Order order = orderService.findById(id);
        if (order == null) throw new NotFoundException("找不到请求的订单信息");
        User user = (User) session.getAttribute("user");
        if (user.getId().equals(order.getCustomerId()) && order.getStatus().equals(OrderStatus.UNPAID.name())) {
            orderService.updateStatus(id, OrderStatus.PAID.name());
            CookieUtil.addMessage(response, "user",
                    new Message(Message.TYPE_SUCCESS, "支付成功"), "/");
            return "redirect:/order/view/" + id;
        } else {
            throw new NotAcceptableException("请求非法，无法处理支付");
        }
    }

    @GetMapping("/order/accept/{id}")
    public String acceptOrder(HttpSession session, HttpServletResponse response,
                              @PathVariable("id") Integer id, @RequestParam("action") String action) {
        Order order = orderService.findById(id);
        if (order == null) throw new NotFoundException("找不到请求的订单信息");
        User user = (User) session.getAttribute("user");
        boolean success = false;
        switch (action) {
            case "request":
                if (user.getId().equals(order.getDevId()) && order.getStatus().equals(OrderStatus.PAID.name())) {
                    orderService.updateStatus(id, OrderStatus.PENDING.name());
                    CookieUtil.addMessage(response, "user",
                            new Message(Message.TYPE_SUCCESS, "申请成功，请等待需求方验收"), "/");
                    success = true;
                }
                break;
            case "pass":
                if (user.getId().equals(order.getCustomerId()) && order.getStatus().equals(OrderStatus.PENDING.name())) {
                    orderService.completeOrder(id, new Date());
                    CookieUtil.addMessage(response, "user",
                            new Message(Message.TYPE_SUCCESS, "已经通过验收"), "/");
                    success = true;
                }
                break;
        }
        if (!success) throw new NotAcceptableException("请求非法，无法处理验收过程");
        return "redirect:/order/view/" + id;
    }

    @PostMapping("/order/comment")
    public String orderComment(HttpSession session, HttpServletResponse response,
                               @RequestParam("orderId") Integer id, @RequestParam("type") String type,
                               @RequestParam("comment") String comment) {
        User user = (User) session.getAttribute("user");
        Order order = orderService.findById(id);
        if (order == null) throw new NotFoundException("找不到请求的订单信息");
        OrderComment orderComment = orderService.findCommentByOrderId(id);
        switch (type) {
            case "dev":
                if (!order.getDevId().equals(user.getId())) throw new NotAcceptableException("非法请求，无法发表评价");
                if (orderComment == null || orderComment.getDevComment() == null) {
                    orderService.devComment(id, comment);
                } else {
                    throw new NotAcceptableException("不能重复发表评价");
                }
                break;
            case "customer":
                if (!order.getCustomerId().equals(user.getId())) throw new NotAcceptableException("非法请求，无法发表评价");
                if (orderComment == null || orderComment.getCustomerComment() == null) {
                    orderService.customerComment(id, comment);
                } else {
                    throw new NotAcceptableException("不能重复发表评价");
                }
                break;
            default:
                throw new NotAcceptableException("无法处理请求");
        }
        CookieUtil.addMessage(response, "user", new Message(Message.TYPE_SUCCESS, "评价已经发表"), "/");
        return "redirect:/order/view/" + id;
    }
}
