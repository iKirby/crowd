package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Message;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.VerifyRequest;
import com.everyone.crowd.entity.exception.NotAcceptableException;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.VerifyRequestService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class VerifyRequestManageController {

    private final VerifyRequestService verifyRequestService;
    private final DevProfileService devProfileService;
    private final CustomerProfileService customerProfileService;

    public VerifyRequestManageController(VerifyRequestService verifyRequestService,
                                         DevProfileService devProfileService,
                                         CustomerProfileService customerProfileService) {
        this.verifyRequestService = verifyRequestService;
        this.devProfileService = devProfileService;
        this.customerProfileService = customerProfileService;
    }

    @GetMapping("/admin/user/verify")
    public String verifyReqList(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "processed", defaultValue = "false") boolean processed) {
        Page<VerifyRequest> verifyRequestPage = verifyRequestService.findByStatus(processed, 20, page);
        model.addAttribute("verifyRequests", verifyRequestPage);
        model.addAttribute("processed", processed);

        List<Integer> devIds = new ArrayList<>();
        List<Integer> customerIds = new ArrayList<>();
        for (VerifyRequest request : verifyRequestPage.getContent()) {
            switch (request.getType()) {
                case "DEVELOPER":
                    devIds.add(request.getUserId());
                    break;
                case "CUSTOMER":
                    customerIds.add(request.getUserId());
                    break;
            }
        }

        if (!devIds.isEmpty()) {
            Map<Integer, String> devIdNameMap = devProfileService.getIdNameMap(devIds);
            model.addAttribute("devIdNameMap", devIdNameMap);
        }
        if (!customerIds.isEmpty()) {
            Map<Integer, String> customerIdNameMap = customerProfileService.getIdNameMap(customerIds);
            model.addAttribute("customerIdNameMap", customerIdNameMap);
        }

        return "admin/verify-manage";
    }

    @GetMapping("/admin/user/verify/view/{id}")
    public String viewVerifyReq(Model model, HttpServletResponse response,
                                @PathVariable("id") Integer id,
                                @RequestParam(value = "pass", required = false) Boolean pass) {
        VerifyRequest request = verifyRequestService.findById(id);
        if (pass != null && !request.isProcessed()) {
            verifyRequestService.process(id, pass);
            request.setProcessed(true);
            String message = pass ? "已经标为通过审核" : "已标为未通过审核";
            CookieUtil.addMessage(response, "admin",
                    new Message(Message.TYPE_SUCCESS, message), "/admin");
        }

        model.addAttribute("verifyRequest", request);

        if (request.getType().equals("DEVELOPER")) {
            model.addAttribute("profile", devProfileService.findById(request.getUserId()));
        } else {
            model.addAttribute("profile", customerProfileService.findById(request.getUserId()));
        }

        return "admin/verify-view";
    }

    @GetMapping("/admin/user/verify/delete/{id}")
    public String deleteVerifyReq(HttpServletResponse response, @PathVariable("id") Integer id) {
        VerifyRequest request = verifyRequestService.findById(id);
        if (request.isProcessed()) {
            CookieUtil.addMessage(response, "admin",
                    new Message(Message.TYPE_SUCCESS, "认证请求已经删除"), "/admin");
            verifyRequestService.delete(id);
            return "redirect:/admin/user/verify?processed=true";
        } else {
            throw new NotAcceptableException("认证请求未处理，无法删除");
        }
    }
}
