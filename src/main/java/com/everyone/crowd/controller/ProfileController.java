package com.everyone.crowd.controller;

import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.entity.status.ProfileStatus;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {
    private final DevProfileService devProfileService;
    private final CustomerProfileService customerProfileService;

    @Autowired
    public ProfileController(DevProfileService devProfileService, CustomerProfileService customerProfileService) {
        this.devProfileService = devProfileService;
        this.customerProfileService = customerProfileService;
    }

    @PostMapping("/user/profile/updateDev")
    public String updateDevProfile(HttpSession session, DevProfile devProfile) {
        User user = (User) session.getAttribute("user");
        devProfile.setUserId(user.getId());
        devProfile.setStatus(ProfileStatus.UNVERIFIED.name());
        if (devProfileService.findById(user.getId()) != null) {
            devProfileService.update(devProfile);
        } else {
            devProfileService.insert(devProfile);
        }
        return "redirect:/user/profile?page=dev";
    }

    @PostMapping("/user/profile/updateCustomer")
    public String updateCustomerProfile(HttpSession session, CustomerProfile customerProfile) {
        User user = (User) session.getAttribute("user");
        customerProfile.setUserId(user.getId());
        customerProfile.setStatus(ProfileStatus.UNVERIFIED.name());
        if (customerProfileService.findById(user.getId()) != null) {
            customerProfileService.update(customerProfile);
        } else {
            customerProfileService.insert(customerProfile);
        }
        return "redirect:/user/profile?page=customer";
    }


}
