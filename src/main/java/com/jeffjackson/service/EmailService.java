package com.jeffjackson.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine; // Optional, for HTML templates

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Send simple text email
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    /**
     * Send HTML email
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    /**
     * Send email with attachment
     */
    public void sendEmailWithAttachment(String to, String subject, String text, String attachmentPath)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        File file = new File(attachmentPath);
        helper.addAttachment(file.getName(), file);

        mailSender.send(message);
    }

    /**
     * Send email using Thymeleaf template
     */
    public void sendEmailFromTemplate(String to, String subject, String templateName,
                                      Map<String, Object> templateModel) throws MessagingException {
        Context context = new Context();
        context.setVariables(templateModel);
        String htmlContent = templateEngine.process(templateName, context);
        sendHtmlEmail(to, subject, htmlContent);
    }

    public void sendEmailFromTemplateWithCc(String to, String[] cc, String subject,
                                            String templateName, Map<String, Object> templateModel)
            throws MessagingException {
        Context context = new Context();
        context.setVariables(templateModel);
        String htmlContent = templateEngine.process(templateName, context);
        sendHtmlEmailWithCc(to, cc, subject, htmlContent);
    }

    private void sendHtmlEmailWithCc(String to, String[] cc, String subject, String htmlContent)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        // Set from address (you might want to make this configurable)
        helper.setFrom("noreply@djjeffjackson.com");

        mailSender.send(message);
    }
}