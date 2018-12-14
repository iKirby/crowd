package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.CategoryService;
import com.everyone.crowd.service.CustomerProfileService;
import com.everyone.crowd.service.DemandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DemandManageController {

    private final DemandService demandService;
    private final CategoryService categoryService;
    private final CustomerProfileService customerProfileService;

    public DemandManageController(DemandService demandService, CategoryService categoryService, CustomerProfileService customerProfileService) {
        this.demandService = demandService;
        this.categoryService = categoryService;
        this.customerProfileService = customerProfileService;
    }

    @GetMapping("/admin/demand")
    public String demandList(Model model,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Demand> demandPage;
        String cardTitle;
        if (keyword.isEmpty()) {
            demandPage = demandService.findAll(20, page);
            cardTitle = "全部需求";
        } else {
            demandPage = demandService.findByMultipleConditions(keyword, null, null, null, null, null, null, null, null, null, 20, page);
            cardTitle = "搜索结果";
        }
        model.addAttribute("cardTitle", cardTitle);
        model.addAttribute("keyword", keyword);
        model.addAttribute("demands", demandPage);
        model.addAttribute("categoryMap", categoryService.getIdNameMap());

        List<Integer> customerIds = new ArrayList<>();
        for (Demand demand : demandPage.getContent()) {
            customerIds.add(demand.getCustomerId());
        }
        model.addAttribute("customerMap", customerProfileService.getIdNameMap(customerIds));
        model.addAttribute("statusMap", demandService.getStatusMap());
        return "admin/demand-manage";
    }

    @GetMapping("/admin/demand/edit/{id}")
    public String editDemand(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("demand", demandService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("statusMap", demandService.getStatusMap());
        return "admin/demand-edit";
    }

    @PostMapping("/admin/demand/edit")
    public String editDemand(Demand demand) {
        demandService.update(demand);
        return "redirect:/admin/demand";
    }

    @GetMapping("/admin/demand/delete/{id}")
    public String deleteDemand(@PathVariable("id") Integer id) {
        demandService.delete(id);
        return "redirect:/admin/demand";
    }
}
