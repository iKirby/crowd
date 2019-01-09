package com.everyone.crowd.controller;

import com.everyone.crowd.entity.*;
import com.everyone.crowd.entity.exception.NotAcceptableException;
import com.everyone.crowd.entity.exception.NotFoundException;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.*;
import com.everyone.crowd.util.CookieUtil;
import com.everyone.crowd.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Value("${com.everyone.crowd.upload.path}")
    private String uploadPath;

    private final DemandService demandService;
    private final CategoryService categoryService;
    private final CustomerProfileService customerProfileService;
    private final AnnouncementService announcementService;
    private final SettingsService settingsService;

    @Autowired
    public MainController(DemandService demandService, CategoryService categoryService,
                          CustomerProfileService customerProfileService,
                          AnnouncementService announcementService, SettingsService settingsService) {
        this.demandService = demandService;
        this.categoryService = categoryService;
        this.customerProfileService = customerProfileService;
        this.announcementService = announcementService;
        this.settingsService = settingsService;
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
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryMap", categoryService.getIdNameMap());
        if (categoryId == 0) {
            model.addAttribute("demands", demandService.findByStatus(DemandStatus.PASS.name(), 10, page));
        } else {
            model.addAttribute("demands", demandService.findByCategoryIdAndStatus(categoryId, DemandStatus.PASS.name(), 10, page));
        }
        model.addAttribute("isSearch", false);
        model.addAttribute("announcements", announcementService.findAllTillNow(10, 1));
        model.addAttribute("title", "需求大厅");
        model.addAttribute("keyword", "");

        if (page == 1 && categoryId == 0) {
            Map<String, String> carouselSettings = settingsService.get("carousel_title", "carousel_img", "carousel_link");
            List<String> carouselTitle = Arrays.stream(carouselSettings.get("carousel_title").split("\r\n|\r")).collect(Collectors.toList());
            if (!carouselTitle.isEmpty() && !carouselTitle.get(0).isEmpty()) {
                List<String> carouselImg = Arrays.stream(carouselSettings.get("carousel_img").split("\r\n|\r")).collect(Collectors.toList());
                List<String> carouselLink = Arrays.stream(carouselSettings.get("carousel_link").split("\r\n|\r")).collect(Collectors.toList());
                model.addAttribute("carouselTitle", carouselTitle);
                model.addAttribute("carouselImg", carouselImg);
                model.addAttribute("carouselLink", carouselLink);
            }
        }
        return "index";
    }

    @GetMapping("/demand/view/{id}")
    public String demandPage(Model model, @PathVariable("id") Integer id) {
        Demand demand = demandService.findById(id);
        if (demand == null) throw new NotFoundException("找不到请求的需求");
        model.addAttribute("demand", demand);
        Category category = categoryService.findById(demand.getCategoryId());
        CustomerProfile profile = customerProfileService.findById(demand.getCustomerId());
        model.addAttribute("customerProfile", profile);
        model.addAttribute("category", category);
        return "demand-view";
    }

    @GetMapping("/demand/new")
    public String newDemand(Model model) {
        model.addAttribute("demand", new Demand());
        model.addAttribute("categories", categoryService.findAll());
        return "demand-new";
    }

    @PostMapping("/demand/new")
    public String newSetDemand(HttpServletResponse response, Demand demand, HttpSession session) {
        User user = (User) session.getAttribute("user");
        demand.setCustomerId(user.getId());
        demand.setPublishTime(new Date());
        demand.setStatus(DemandStatus.PENDING.name());
        demandService.insert(demand);
        CookieUtil.addMessage(response, "user",
                new Message(Message.TYPE_SUCCESS, "需求已经发布，请等待审核"), "/");
        return "redirect:/demand/view/" + demand.getId();
    }

    @GetMapping("/demand/edit/{id}")
    public String editDemand(Model model, HttpSession session, @PathVariable("id") Integer id) {
        Demand demand = demandService.findById(id);
        if (demand == null) throw new NotFoundException("找不到请求的需求信息");
        User user = (User) session.getAttribute("user");
        if (!demand.getCustomerId().equals(user.getId()) || demand.getStatus().equals(DemandStatus.CONTRACTED.name())) {
            throw new NotAcceptableException("您无法编辑此需求");
        }
        model.addAttribute("demand", demand);
        model.addAttribute("categories", categoryService.findAll());
        return "demand-update";
    }

    @PostMapping("/demand/edit")
    public String editSetDemand(HttpServletResponse response, Demand demand, HttpSession session) {
        User user = (User) session.getAttribute("user");
        demand.setCustomerId(user.getId());
        Demand current = demandService.findById(demand.getId());
        if (!current.getCustomerId().equals(user.getId()) || current.getStatus().equals(DemandStatus.CONTRACTED.name())) {
            throw new NotAcceptableException("您无法编辑此需求");
        }
        if (demand.getAttachment() == null) {
            demand.setAttachment(current.getAttachment());
        } else {
            FileUtil.deleteFile(uploadPath, current.getAttachment());
        }
        demand.setStatus(current.getStatus());
        demand.setPublishTime(current.getPublishTime());
        demandService.update(demand);
        CookieUtil.addMessage(response, "user",
                new Message(Message.TYPE_SUCCESS, "更改已经保存"), "/");
        return "redirect:/demand/view/" + demand.getId();
    }

    @GetMapping("/demand/delete/{id}")
    public String deleteDemand(HttpServletResponse response, HttpSession session, @PathVariable("id") Integer id) {
        Demand demand = demandService.findById(id);
        if (demand != null && !demand.getStatus().equals(DemandStatus.CONTRACTED.name()) &&
                demand.getCustomerId().equals(((User) session.getAttribute("user")).getId())) {
            demandService.delete(id);
            CookieUtil.addMessage(response, "user",
                    new Message(Message.TYPE_SUCCESS, "需求信息已经删除"), "/");
            return "redirect:/demand/my";
        } else {
            throw new NotAcceptableException("非法请求，无法删除需求");
        }
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
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryMap", categoryService.getIdNameMap());
        model.addAttribute("statusMap", demandService.getStatusMap());
        return "demand-my";
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
        model.addAttribute("categoryMap", categoryService.getIdNameMap());
        model.addAttribute("isSearch", true);
        model.addAttribute("announcements", announcementService.findAllTillNow(10, 1));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("title", "搜索结果");
        return "index";
    }

    @GetMapping("/announcement/{id}")
    public String announcementPage(Model model, @PathVariable("id") Integer id) {
        Announcement announcement = announcementService.findById(id);
        if (announcement == null) throw new NotFoundException("找不到请求的公告信息");
        model.addAttribute("announcement", announcement);
        model.addAttribute("announcements", announcementService.findAllTillNow(10, 1));
        return "announcement";
    }
}
