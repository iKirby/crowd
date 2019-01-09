package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Announcement;
import com.everyone.crowd.entity.Message;
import com.everyone.crowd.service.AnnouncementService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class AnnouncementManageController {
    private final AnnouncementService announcementService;

    public AnnouncementManageController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/admin/announcement")
    public String announcementList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("announcements", announcementService.findAll(20, page));
        return "admin/announcement-manage";
    }

    @GetMapping("/admin/announcement/edit/{id}")
    public String editAnnouncement(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("announcement", announcementService.findById(id));
        model.addAttribute("title", "编辑公告");
        return "admin/announcement-edit";
    }

    @GetMapping("/admin/announcement/new")
    public String editAnnouncement(Model model) {
        model.addAttribute("announcement", new Announcement());
        model.addAttribute("title", "新公告");
        return "admin/announcement-edit";
    }

    @PostMapping("/admin/announcement")
    public String updateAnnouncement(HttpServletResponse response, Announcement announcement) {
        if (announcement.getPublishTime() == null) {
            announcement.setPublishTime(new Date());
        }
        if (announcement.getId() == null) {
            announcementService.insert(announcement);
        } else {
            announcementService.update(announcement);
        }
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "公告已经保存"), "/admin");
        return "redirect:/admin/announcement/edit/" + announcement.getId();
    }

    @GetMapping("/admin/announcement/delete/{id}")
    public String deleteAnnouncement(HttpServletResponse response, @PathVariable("id") Integer id) {
        announcementService.delete(id);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "公告已经删除"), "/admin");
        return "redirect:/admin/announcement";
    }
}
