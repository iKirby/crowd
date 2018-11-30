package com.everyone.crowd.controller;

import com.everyone.crowd.entity.Category;
import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.CategoryService;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MainController {
    private final DemandService demandService;
    private final CategoryService categoryService;
    private final UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @Autowired
    public MainController(DemandService demandService, CategoryService categoryService, UserService userService) {
        this.demandService = demandService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String indexPage(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("demands", demandService.findByStatus("通过审核", 10, page));
        return "index";
    }

    @GetMapping("/demand/{id}")
    public String demandPage(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("demand", demandService.findById(id));
        Category category = categoryService.findById(demandService.findById(id).getCategoryId());
        User user = userService.findById(demandService.findById(id).getCustomerId());
        model.addAttribute("user", user);
        model.addAttribute("category", category);
        return "demand";
    }

    @GetMapping("/newdemand")
    public String newDemand(Model model) {
        model.addAttribute("title", "发布需求");
        model.addAttribute("demand", new Demand());
        return "newdemand";
    }

    @PostMapping("/newsetdemand")
    public String newSetDemand(Model model, Demand demand, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = (User) session.getAttribute("user");
        demand.setCustomerId(user.getId());
        demand.setPublishTime(new Date());
        demand.setStatus(DemandStatus.PENDING.name());
        demandService.insert(demand);
        model.addAttribute("demands", demandService.findAll(10, page));
        return "index";
    }

    @GetMapping("/editdemand/{id}")
    public String editDemand(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("title", "修改需求");
        model.addAttribute("demand", demandService.findById(id));
        return "updatedemand";
    }

    @PostMapping("/editsetdemand/{id}")
    public String editSetDemand(Model model, Demand demand, @PathVariable("id") Integer id) {
        model.addAttribute("title", "修改需求");
        demandService.update(id, demand);
        model.addAttribute("demand", demandService.findById(id));
        return "demand";
    }

    @GetMapping("/deletedemand/{id}")
    public String deleteDemand(Model model, @PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("demands", demandService.findAll(10, page));
        demandService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/viewdemandbyeducation")
    public String viewDemandByEducation(Model model,@RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("demands", demandService.findByCategoryId(1,10, page));
        return "index";
    }

    @GetMapping("/viewdemand")
    public String viewDemand(Model model, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("demands", demandService.findByCustomerId(user.getId(), 10, page));
        return "viewdemand";
    }


}
