package com.everyone.crowd.controller;

import com.everyone.crowd.entity.Category;
import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.CategoryService;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.util.CookieUtil;
import com.everyone.crowd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private final DemandService demandService;
    private final CategoryService categoryService;
    private final UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @Autowired
    public MainController(DemandService demandService, CategoryService categoryService, UserService userService) {
        this.demandService = demandService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String indexPage(Model model, HttpServletRequest request, HttpSession session,
                            @RequestParam(value = "category", defaultValue = "0") int categoryId,
                            @RequestParam(value = "page", defaultValue = "1") int page) {
        if (session.getAttribute("user") == null) {
            String loginCookie = CookieUtil.getCookieValue("USR_LOGIN", request.getCookies());
            if (!loginCookie.isEmpty()) {
                return "redirect:/user/login";
            }
        }

        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryList);
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category aCategory : categoryList) {
            categoryMap.put(aCategory.getId(), aCategory.getName());
        }
        model.addAttribute("categoryMap", categoryMap);

        if (categoryId == 0) {
            model.addAttribute("demands", demandService.findByStatus(DemandStatus.PASS.name(), 10, page));
        } else {
            // TODO 添加条件查询
            model.addAttribute("demands", demandService.findByCategoryIdAndStatus(categoryId, DemandStatus.PASS.name(), 10, page));
        }
        return "index";
    }

    @GetMapping("/demand/view/{id}")
    public String demandPage(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("demand", demandService.findById(id));
        Category category = categoryService.findById(demandService.findById(id).getCategoryId());
        User user = userService.findById(demandService.findById(id).getCustomerId());
        model.addAttribute("user", user);
        model.addAttribute("category", category);
        return "demand";
    }

    @GetMapping("/demand/new")
    public String newDemand(Model model) {
        model.addAttribute("title", "发布需求");
        model.addAttribute("demand", new Demand());
        model.addAttribute("categories", categoryService.findAll());
        return "newdemand";
    }

    @PostMapping("/demand/new")
    public String newSetDemand(Demand demand, HttpSession session) {
        User user = (User) session.getAttribute("user");
        demand.setCustomerId(user.getId());
        demand.setPublishTime(new Date());
        demand.setStatus(DemandStatus.PENDING.name());
        demandService.insert(demand);
        return "redirect:/demand/view/" + demand.getId();
    }

    @GetMapping("/demand/edit/{id}")
    public String editDemand(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("title", "修改需求");
        model.addAttribute("demand", demandService.findById(id));
        return "updatedemand";
    }

    @PostMapping("/demand/edit")
    public String editSetDemand(Demand demand) {
        demandService.update(demand);
        return "redirect:/demand/view/" + demand.getId();
    }

    @GetMapping("/demand/delete/{id}")
    public String deleteDemand(@PathVariable("id") Integer id) {
        demandService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/demand/my")
    public String viewDemand(Model model, HttpSession session,
                             @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("demands", demandService.findByCustomerId(user.getId(), 10, page));
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category aCategory : categoryList) {
            categoryMap.put(aCategory.getId(), aCategory.getName());
        }
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(DemandStatus.PENDING.name(), "审核中");
        statusMap.put(DemandStatus.PASS.name(), "审核通过");
        statusMap.put(DemandStatus.FAIL.name(), "审核未通过");
        statusMap.put(DemandStatus.CONTRACTED.name(), "竞标中");
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("statusMap", statusMap);
        return "viewdemand";
    }

    @GetMapping("/demand/search")
    public String searchDemand(Model model, @RequestParam("key") String title, @RequestParam(value = "category", defaultValue = "0") int category, @RequestParam(value = "page", defaultValue = "1") int page) {
        if (title.isEmpty()) {
            model.addAttribute("demands", demandService.findByStatus(DemandStatus.PASS.name(), 10, page));
        } else {
            model.addAttribute("demands", demandService.findByTitle(title, 10, page));
        }
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryId", category);
        model.addAttribute("categories", categoryList);
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category aCategory : categoryList) {
            categoryMap.put(aCategory.getId(), aCategory.getName());
        }
        model.addAttribute("categoryMap", categoryMap);
        return "index1";
    }

    @GetMapping("/demand/search/customer")
    public String searchCustomerDemand(Model model, HttpSession session, @RequestParam("key1") String title, @RequestParam(value = "category", defaultValue = "0") int category, @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = (User) session.getAttribute("user");
        if (title.isEmpty()) {
            model.addAttribute("demands", demandService.findByCustomerId(user.getId(), 10, page));
        } else {
            model.addAttribute("demands", demandService.findByCustomerIdAndTitle(user.getId(), title, 10, page));
        }
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryId", category);
        model.addAttribute("categories", categoryList);
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category aCategory : categoryList) {
            categoryMap.put(aCategory.getId(), aCategory.getName());
        }
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put(DemandStatus.PENDING.name(), "审核中");
        statusMap.put(DemandStatus.PASS.name(), "审核通过");
        statusMap.put(DemandStatus.FAIL.name(), "审核未通过");
        statusMap.put(DemandStatus.CONTRACTED.name(), "竞标中");
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("statusMap", statusMap);
        return "viewdemand";
    }

}
