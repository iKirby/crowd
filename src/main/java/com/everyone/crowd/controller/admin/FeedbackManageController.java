package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Feedback;
import com.everyone.crowd.entity.Message;
import com.everyone.crowd.entity.Page;
import com.everyone.crowd.service.FeedbackService;
import com.everyone.crowd.service.UserService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class FeedbackManageController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    public FeedbackManageController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @GetMapping("/admin/feedback")
    public String feedbackList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Feedback> feedbackPage = feedbackService.findAll(20, page);
        model.addAttribute("feedbacks", feedbackPage);
        return "admin/feedback-manage";
    }

    @GetMapping("/admin/feedback/view/{id}")
    public String viewFeedback(Model model, @PathVariable("id") Integer id) {
        Feedback feedback = feedbackService.findById(id);
        model.addAttribute("feedback", feedback);
        model.addAttribute("username", userService.findById(id).getUsername());
        return "admin/feedback-view";
    }

    @PostMapping("/admin/feedback")
    public String updateFeedbackReply(HttpServletResponse response,
                                      @RequestParam("id") Integer id, @RequestParam("reply") String reply) {
        feedbackService.reply(id, reply);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "回复已经保存"), "/admin");
        return "redirect:/admin/feedback/view/" + id;
    }

    @GetMapping("/admin/feedback/delete/{id}")
    public String deleteFeedback(HttpServletResponse response, @PathVariable("id") Integer id) {
        feedbackService.delete(id);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "反馈已经删除"), "/admin");
        return "redirect:/admin/feedback";
    }
}
