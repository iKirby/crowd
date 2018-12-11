package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.entity.status.DemandStatus;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String demandList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Demand> demandPage = demandService.findAll(20, page);
        model.addAttribute("demands", demandPage);
        model.addAttribute("categoryMap", categoryService.getIdNameMap());

        List<Integer> customerIds = new ArrayList<>();
        for (Demand demand : demandPage.getContent()) {
            customerIds.add(demand.getCustomerId());
        }
        model.addAttribute("customerMap", customerProfileService.getIdNameMap(customerIds));

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(DemandStatus.PENDING.name(), "审核中");
        statusMap.put(DemandStatus.PASS.name(), "审核通过");
        statusMap.put(DemandStatus.FAIL.name(), "审核未通过");
        statusMap.put(DemandStatus.CONTRACTED.name(), "竞标中");
        model.addAttribute("statusMap", statusMap);
        return "admin/demandmanage";
    }

    @GetMapping("/admin/demand/edit/{id}")
    public String editDemand(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("demand", demandService.findById(id));
        model.addAttribute("categories", categoryService.findAll());

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(DemandStatus.PENDING.name(), "审核中");
        statusMap.put(DemandStatus.PASS.name(), "审核通过");
        statusMap.put(DemandStatus.FAIL.name(), "审核未通过");
        statusMap.put(DemandStatus.CONTRACTED.name(), "竞标中");
        model.addAttribute("statusMap", statusMap);
        return "admin/demandedit";
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
