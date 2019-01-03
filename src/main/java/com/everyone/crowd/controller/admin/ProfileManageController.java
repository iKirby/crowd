package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.CustomerProfile;
import com.everyone.crowd.entity.DevProfile;
import com.everyone.crowd.entity.Message;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.status.ProfileStatus;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DevProfileService;
import com.everyone.crowd.util.CookieUtil;
import com.everyone.crowd.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/admin/user/customerprofile")
    public String customerProfileList(Model model,
                                      @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                      @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<CustomerProfile> customerProfilePage;
        String cardTitle;
        if (keyword.isEmpty()) {
            customerProfilePage = customerProfileService.findAll(20, page);
            cardTitle = "全部需求方";
        } else {
            customerProfilePage = customerProfileService.findByName(keyword, 20, page);
            cardTitle = "搜索结果";
        }
        model.addAttribute("profiles", customerProfilePage);
        model.addAttribute("title", "需求方管理");
        model.addAttribute("cardTitle", cardTitle);
        model.addAttribute("user", "customerprofile");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-manage";
    }

    @GetMapping("/admin/user/developerprofile")
    public String developerProfileList(Model model,
                                       @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                       @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<DevProfile> devProfilePage;
        String cardTitle;
        if (keyword.isEmpty()) {
            devProfilePage = devProfileService.findAll(20, page);
            cardTitle = "全部开发者";
        } else {
            devProfilePage = devProfileService.findByName(keyword, 20, page);
            cardTitle = "搜索结果";
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("profiles", devProfilePage);
        model.addAttribute("title", "开发者管理");
        model.addAttribute("cardTitle", cardTitle);
        model.addAttribute("user", "developerprofile");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(ProfileStatus.PENDING.name(), "审核中");
        statusMap.put(ProfileStatus.UNVERIFIED.name(), "未审核");
        statusMap.put(ProfileStatus.VERIFIED.name(), "审核通过");
        statusMap.put(ProfileStatus.FAILED.name(), "审核未通过");
        model.addAttribute("statusMap", statusMap);
        return "admin/profile-manage";
    }

    @GetMapping("/admin/user/customerprofile/edit/{userId}")
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

    @GetMapping("/admin/user/developerprofile/edit/{userId}")
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

    @GetMapping("/admin/user/customerprofile/delete/{userId}")
    public String deleteCustomerProfile(HttpServletResponse response, @PathVariable("userId") Integer userId) {
        customerProfileService.delete(userId);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "资料已经删除"), "/admin");
        return "redirect:/admin/user/customerprofile";
    }

    @GetMapping("/admin/user/developerprofile/delete/{userId}")
    public String deleteDeveloperProfile(HttpServletResponse response, @PathVariable("userId") Integer userId) {
        devProfileService.delete(userId);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "资料已经删除"), "/admin");
        return "redirect:/admin/user/developerprofile";
    }

    @PostMapping("/admin/user/customerprofile/edit")
    public String editCustomerProfile(HttpServletResponse response, CustomerProfile customerProfile) {
        CustomerProfile current = customerProfileService.findById(customerProfile.getUserId());
        if (customerProfile.getPhoto() == null) {
            customerProfile.setPhoto(current.getPhoto());
        } else {
            FileUtil.deleteFile(uploadPath, current.getPhoto());
        }
        customerProfileService.update(customerProfile);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "更改已经保存"), "/admin");
        return "redirect:/admin/user/customerprofile";
    }

    @PostMapping("/admin/user/developerprofile/edit")
    public String editDeveloperProfile(HttpServletResponse response, DevProfile devProfile) {
        DevProfile current = devProfileService.findById(devProfile.getUserId());
        if (devProfile.getPhoto() == null) {
            devProfile.setPhoto(current.getPhoto());
        } else {
            FileUtil.deleteFile(uploadPath, current.getPhoto());
        }
        devProfileService.update(devProfile);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "更改已经保存"), "/admin");
        return "redirect:/admin/user/developerprofile";
    }

}
