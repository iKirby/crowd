package com.everyone.crowd.controller;

import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.service.CategoryService;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.util.CookieUtil;
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

@Controller
public class MainController {
    private final DemandService demandService;
    private final CategoryService categoryService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @Autowired
    public MainController(DemandService demandService, CategoryService categoryService) {
        this.demandService = demandService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String indexPage(Model model, HttpServletRequest request, HttpSession session,
                            @RequestParam(value = "categoryId", defaultValue = "0") int category,
                            @RequestParam(value = "page", defaultValue = "1") int page) {
        if (session.getAttribute("user") == null) {
            String loginCookie = CookieUtil.getCookieValue("USR_LOGIN", request.getCookies());
            if (!loginCookie.isEmpty()) {
                return "redirect:/user/login";
            }
        }
        model.addAttribute("categoryId", category);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("demands", demandService.findAll(10, page));
        return "index";
    }

    @GetMapping("/demand/{id}")
    public String demandPage(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("demand", demandService.findById(id));
        return "demand";
    }

    @GetMapping("/newdemand")
    public String newDemand(Model model) {
        model.addAttribute("title", "发布需求");
        return "newdemand";
    }

    @PostMapping("/newsetdemand")
    public String newSetDemand(Model model, Demand demand, HttpSession session) {
        User user = (User) session.getAttribute("user");
        demand.setCustomerId(user.getId());
        demandService.insert(demand);
        model.addAttribute("demand", new Demand());
        return "newdemand";
    }

    @GetMapping("/editdemand/{id}")
    public String editDemand(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("title", "修改需求");
        model.addAttribute("demand", demandService.findById(id));
        return "newdemand";
    }

    @PostMapping("/editsetdemand/{id}")
    public String editSetDemand(Model model, Demand demand, @PathVariable("id") Integer id) {
        model.addAttribute("title", "修改需求");
        demandService.update(id, demand);
        return "demand";
    }


}
