package com.everyone.crowd.controller;

import com.everyone.crowd.entity.Feedback;
import com.everyone.crowd.entity.User;
import com.everyone.crowd.entity.exception.NotFoundException;
import com.everyone.crowd.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedback/new")
    public String newFeedback(Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        Feedback feedback = new Feedback();
        feedback.setUrl(referer);
        model.addAttribute("feedback", feedback);
        return "feedback-new";
    }

    @PostMapping("/feedback/new")
    public String newFeedback(Feedback feedback, HttpSession session) {
        User user = (User) session.getAttribute("user");
        feedback.setUserId(user.getId());
        feedback.setSubmitTime(new Date());
        feedbackService.submit(feedback);
        return "redirect:/feedback/view/" + feedback.getId();
    }

    @GetMapping("/feedback/view/{id}")
    public String viewFeedback(Model model, @PathVariable("id") Integer id) {
        Feedback feedback = feedbackService.findById(id);
        if (feedback == null) throw new NotFoundException("找不到请求的反馈信息");
        model.addAttribute("feedback", feedback);
        return "feedback";
    }
}
