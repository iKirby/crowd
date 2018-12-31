package com.everyone.crowd.controller;

import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.entity.VerifyRequest;
import com.everyone.crowd.entity.status.ProfileStatus;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.service.VerifyRequestService;
import com.everyone.crowd.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {
    @Value("${com.everyone.crowd.upload.path}")
    private String uploadPath;

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
        DevProfile current = devProfileService.findById(user.getId());
        if (current != null) {
            if (devProfile.getPhoto() == null) {
                devProfile.setPhoto(current.getPhoto());
            } else {
                FileUtil.deleteFile(uploadPath, current.getPhoto());
            }
            devProfile.setLevel(current.getLevel());
            devProfile.setStatus(current.getStatus());
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
        CustomerProfile current = customerProfileService.findById(user.getId());
        if (current != null) {
            if (customerProfile.getPhoto() == null) {
                customerProfile.setPhoto(current.getPhoto());
            } else {
                FileUtil.deleteFile(uploadPath, current.getPhoto());
            }
            customerProfile.setLevel(current.getLevel());
            customerProfile.setStatus(current.getStatus());
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
        return "verify-new";
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
