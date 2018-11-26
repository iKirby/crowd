package com.everyone.crowd.service.impl;

import com.everyone.crowd.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine thymeleafEngine;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine thymeleafEngine) {
        this.mailSender = mailSender;
        this.thymeleafEngine = thymeleafEngine;
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
        messageHelper.setSubject(subject);
        messageHelper.setTo(to);
        messageHelper.setText(text, true);
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendActivateEmail(String to, String username, String activateCode) throws MessagingException {
        String link = "http://localhost:8080/user/login?action=activate&activateCode=" + activateCode;
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("link", link);
        String content = thymeleafEngine.process("activate-mail", context);
        sendHtmlMessage(to, "账户激活 - CROWD 众包平台", content);
    }
}
