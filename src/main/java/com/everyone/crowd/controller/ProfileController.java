package com.everyone.crowd.controller;

import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.entity.VerifyRequest;
import com.everyone.crowd.entity.status.ProfileStatus;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.VerifyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {
    private final DevProfileService devProfileService;
    private final CustomerProfileService customerProfileService;
    private final VerifyRequestService verifyRequestService;

    @Autowired
    public ProfileController(DevProfileService devProfileService,
                             CustomerProfileService customerProfileService,
                             VerifyRequestService verifyRequestService) {
        this.devProfileService = devProfileService;
        this.customerProfileService = customerProfileService;
        this.verifyRequestService = verifyRequestService;
    }

    @PostMapping("/user/profile/updateDev")
    public String updateDevProfile(HttpSession session, DevProfile devProfile) {
        User user = (User) session.getAttribute("user");
        devProfile.setUserId(user.getId());
        if (devProfileService.findById(user.getId()) != null) {
            devProfileService.update(devProfile);
        } else {
            devProfile.setStatus(ProfileStatus.UNVERIFIED.name());
            devProfileService.insert(devProfile);
        }
        return "redirect:/user/profile?page=dev";
    }

    @PostMapping("/user/profile/updateCustomer")
    public String updateCustomerProfile(HttpSession session, CustomerProfile customerProfile) {
        User user = (User) session.getAttribute("user");
        customerProfile.setUserId(user.getId());
        if (customerProfileService.findById(user.getId()) != null) {
            customerProfileService.update(customerProfile);
        } else {
            customerProfile.setStatus(ProfileStatus.UNVERIFIED.name());
            customerProfileService.insert(customerProfile);
        }
        return "redirect:/user/profile?page=customer";
    }

    @GetMapping("/user/profile/verify")
    public String verifyPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("verifyRequest", new VerifyRequest());
        DevProfile devProfile = devProfileService.findById(user.getId());
        CustomerProfile customerProfile = customerProfileService.findById(user.getId());
        if (devProfile == null) {
            devProfile = new DevProfile();
        }
        if (customerProfile == null) {
            customerProfile = new CustomerProfile();
        }
        model.addAttribute("devProfile", devProfile);
        model.addAttribute("customerProfile", customerProfile);
        return "verify";
    }

    @PostMapping("/user/profile/verify")
    public String verifySubmit(HttpSession session, VerifyRequest request) {
        User user = (User) session.getAttribute("user");
        request.setUserId(user.getId());
        verifyRequestService.request(request);
        switch (request.getType()) {
            case "DEVELOPER":
                devProfileService.updateStatus(user.getId(), ProfileStatus.PENDING.name());
                break;
            case "CUSTOMER":
                customerProfileService.updateStatus(user.getId(), ProfileStatus.PENDING.name());
                break;
        }
        return "redirect:/user/profile/verify";
    }

}
