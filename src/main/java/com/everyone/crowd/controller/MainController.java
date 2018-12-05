package com.everyone.crowd.controller;

import com.everyone.crowd.entity.Category;
import com.everyone.crowd.entity.Demand;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.AnnouncementService;
import com.everyone.crowd.service.CategoryService;
import com.everyone.crowd.service.DemandService;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.CookieUtil;
import com.everyone.crowd.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Value("${com.everyone.crowd.upload.path}")
    private String uploadPath;

    private final DemandService demandService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final AnnouncementService announcementService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @Autowired
    public MainController(DemandService demandService, CategoryService categoryService, UserService userService, AnnouncementService announcementService) {
        this.demandService = demandService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.announcementService = announcementService;
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
            model.addAttribute("demands", demandService.findByCategoryIdAndStatus(categoryId, DemandStatus.PASS.name(), 10, page));
        }
        model.addAttribute("isSearch", false);
        model.addAttribute("announcements", announcementService.findAll(10, 1));
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
        model.addAttribute("demand", demandService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "updatedemand";
    }

    @PostMapping("/demand/edit")
    public String editSetDemand(Demand demand, HttpSession session) {
        User user = (User) session.getAttribute("user");
        demand.setCustomerId(user.getId());
        Demand current = demandService.findById(demand.getId());
        if (demand.getAttachment() == null) {
            demand.setAttachment(current.getAttachment());
        } else {
            FileUtil.deleteFile(uploadPath, current.getAttachment());
        }
        demandService.update(demand);
        return "redirect:/demand/view/" + demand.getId();
    }

    @GetMapping("/demand/delete/{id}")
    public String deleteDemand(@PathVariable("id") Integer id) {
        demandService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/demand/my")
    public String searchCustomerDemand(Model model, HttpSession session,
                                       @RequestParam(value = "category", defaultValue = "0") int categoryId,
                                       @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = (User) session.getAttribute("user");
        if (categoryId == 0) {
            model.addAttribute("demands", demandService.findByCustomerId(user.getId(), 10, page));
        } else {
            model.addAttribute("demands", demandService.findByCustomerIdAndCategoryId(user.getId(), categoryId, 10, page));
        }
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryId", categoryId);
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
    public String searchDemand(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               @RequestParam(value = "region", required = false) String region,
                               @RequestParam(value = "lowPrice", required = false) BigDecimal lowPrice,
                               @RequestParam(value = "highPrice", required = false) BigDecimal highPrice,
                               @RequestParam(value = "startDateFrom", required = false) Date startDateFrom,
                               @RequestParam(value = "startDateTo", required = false) Date startDateTo,
                               @RequestParam(value = "endDateFrom", required = false) Date endDateFrom,
                               @RequestParam(value = "endDateTo", required = false) Date endDateTo,
                               @RequestParam(value = "page", defaultValue = "1") int page) {
        if (keyword == null && categoryId == null && region == null && lowPrice == null && highPrice == null
                && startDateFrom == null && startDateTo == null && endDateFrom == null && endDateTo == null) {
            model.addAttribute("categories", categoryService.findAll());
            return "search";
        }
        if (categoryId != null && categoryId == 0) {
            categoryId = null;
        }
        model.addAttribute("demands", demandService.findByMultipleConditions(keyword,
                categoryId, region, lowPrice, highPrice, startDateFrom, startDateTo, endDateFrom, endDateTo,
                DemandStatus.PASS.name(), 10, page));
        List<Category> categoryList = categoryService.findAll();
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category aCategory : categoryList) {
            categoryMap.put(aCategory.getId(), aCategory.getName());
        }
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("isSearch", true);
        return "index";
    }

    @GetMapping("/announcements/{id}")
    public String announcementPage(Model model, @PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("announcement", announcementService.findById(id));
        model.addAttribute("announcements", announcementService.findAll(5, page));
        return "announcement";
    }
}
