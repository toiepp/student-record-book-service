package com.mikholskiy.recordbook.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

    public void sendApprovalEmail(String userEmail, String generatedPassword) {
        String subject = "Registration Approved";
        String body = "Congratulations! Your registration has been approved. Your password is: " + generatedPassword;
        sendEmail(userEmail, subject, body);
    }
}

