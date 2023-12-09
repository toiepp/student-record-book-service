package com.mikholskiy.recordbook.service;

import com.mikholskiy.recordbook.entity.User;
import com.mikholskiy.recordbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationApprovalService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 86400000) // Каждые 24 часа
    public void sendApprovalReminders() {
        List<User> unapprovedUsers = userRepository.findByIsApprovedFalseAndCreatedDateBefore(
                LocalDateTime.now().minusDays(7));

        for (User user : unapprovedUsers) {
            sendRejectionEmail(user.getEmail());
            adminService.rejectUser(user.getEmail());
        }
    }


    private void sendRejectionEmail(String userEmail) {
        String subject = "Registration Rejected";
        String body = "We regret to inform you that your registration has been rejected.";
        emailService.sendEmail(userEmail, subject, body);
    }


}
