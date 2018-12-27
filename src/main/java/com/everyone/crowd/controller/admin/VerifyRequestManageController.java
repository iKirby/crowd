package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.VerifyRequest;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.VerifyRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        Map<Integer, String> devIdNameMap = devProfileService.getIdNameMap(devIds);
        Map<Integer, String> customerIdNameMap = customerProfileService.getIdNameMap(customerIds);
        model.addAttribute("devIdNameMap", devIdNameMap);
        model.addAttribute("customerIdNameMap", customerIdNameMap);

        return "admin/verify-manage";
    }
}
