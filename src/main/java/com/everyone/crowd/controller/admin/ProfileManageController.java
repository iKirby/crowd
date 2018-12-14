package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.status.ProfileStatus;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileManageController {
    @Value("${com.everyone.crowd.upload.path}")
    private String uploadPath;

    private CustomerProfileService customerProfileService;
    private DevProfileService devProfileService;

    public ProfileManageController(CustomerProfileService customerProfileService, DevProfileService devProfileService) {
        this.customerProfileService = customerProfileService;
        this.devProfileService = devProfileService;
    }

    @GetMapping("/admin/customerprofile")
    public String customerProfileList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<CustomerProfile> customerProfilePage = customerProfileService.findAll(20, page);
        model.addAttribute("profiles", customerProfilePage);
        model.addAttribute("title", "需求方管理");
        model.addAttribute("cardTitle", "全部需求方");
        model.addAttribute("user", "customerprofile");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-manage";
    }

    @GetMapping("/admin/developerprofile")
    public String developerProfileList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<DevProfile> devProfilePage = devProfileService.findAll(20, page);
        model.addAttribute("profiles", devProfilePage);
        model.addAttribute("title", "开发者管理");
        model.addAttribute("cardTitle", "全部开发者");
        model.addAttribute("user", "developerprofile");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-manage";
    }

    @GetMapping("/admin/customerprofile/edit/{userId}")
    public String editCustomerProfile(Model model, @PathVariable("userId") Integer userId) {
        model.addAttribute("profile", customerProfileService.findById(userId));
        model.addAttribute("title", "编辑需求方资料");
        model.addAttribute("user", "customerprofile");

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-edit";
    }

    @GetMapping("/admin/developerprofile/edit/{userId}")
    public String editDeveloperProfile(Model model, @PathVariable("userId") Integer userId) {
        model.addAttribute("profile", devProfileService.findById(userId));
        model.addAttribute("title", "编辑开发者资料");
        model.addAttribute("user", "developerprofile");

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-edit";
    }

    @GetMapping("/admin/customerprofile/delete/{userId}")
    public String deleteCustomerProfile(@PathVariable("userId") Integer userId) {
        customerProfileService.delete(userId);
        return "redirect:/admin/customerprofile";
    }

    @GetMapping("/admin/developerprofile/delete/{userId}")
    public String deleteDeveloperProfile(@PathVariable("userId") Integer userId) {
        devProfileService.delete(userId);
        return "redirect:/admin/developerprofile";
    }

    @PostMapping("/admin/customerprofile/edit")
    public String editCustomerProfile(CustomerProfile customerProfile) {
        CustomerProfile current = customerProfileService.findById(customerProfile.getUserId());
        if (customerProfile.getPhoto() == null) {
            customerProfile.setPhoto(current.getPhoto());
        } else {
            FileUtil.deleteFile(uploadPath, current.getPhoto());
        }
        customerProfileService.update(customerProfile);
        return "redirect:/admin/customerprofile";
    }

    @PostMapping("/admin/developerprofile/edit")
    public String editDeveloperProfile(DevProfile devProfile) {
        DevProfile current = devProfileService.findById(devProfile.getUserId());
        if (devProfile.getPhoto() == null) {
            devProfile.setPhoto(current.getPhoto());
        } else {
            FileUtil.deleteFile(uploadPath, current.getPhoto());
        }
        devProfileService.update(devProfile);
        return "redirect:/admin/developerprofile";
    }


    @GetMapping("/user/customerprofile/search")
    public String searchCustomerProfile(Model model,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("profiles", customerProfileService.findByName(keyword, 20, page));
        model.addAttribute("user", "customerprofile");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-manage";
    }

    @GetMapping("/user/developerprofile/search")
    public String searchDeveloperProfile(Model model,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("profiles", devProfileService.findByName(keyword, 20, page));
        model.addAttribute("user", "developerprofile");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-manage";
    }


}
