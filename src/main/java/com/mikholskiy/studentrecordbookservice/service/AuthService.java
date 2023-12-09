package com.mikholskiy.studentrecordbookservice.service;


import com.mikholskiy.studentrecordbookservice.dto.AuthLoginDTO;
import com.mikholskiy.studentrecordbookservice.entity.User;
import com.mikholskiy.studentrecordbookservice.entity.UserRole;
import com.mikholskiy.studentrecordbookservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public String saveUser(AuthLoginDTO credential) {
        if (repository.findByEmail(credential.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(credential.getEmail());
        user.setRole(credential.getUserRole());
        user.setIsApproved(credential.getUserRole() == UserRole.ADMIN);

        if (credential.getUserRole() == UserRole.ADMIN) {
            user.setPassword(passwordEncoder.encode(credential.getPassword()));
        }

        repository.save(user);
        return "User added to the system";
    }


    public Map<String, String> loginUser(AuthLoginDTO authLoginDTO){
        Map<String, String> json = new HashMap<>();
        String email = authLoginDTO.getEmail();
        String password = authLoginDTO.getPassword();
        json.put("accessToken", "Bearer " + getAccessToken(email, password));
        return json;
    }




    public String generateToken(String email, UserRole role) {
            return jwtService.generateToken(email, role);
        }

    public void validateToken(String token){
        try {
            jwtService.validateToken(token);
            System.out.println("token is valid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Token validation failed");
        }
    }

    public String getAccessToken(String email, String password) {
        Optional<User> userCredentialOptional = repository.findByEmail(email);

        if (userCredentialOptional.isPresent()) {
            User user = userCredentialOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                if (user.getIsApproved()) {
                    System.out.println(user.getRole());
                    return jwtService.generateToken(user.getEmail(), user.getRole());
                } else {
                    throw new RuntimeException("User is not approved");
                }
            } else {
                throw new RuntimeException("Invalid password");
            }
        } else {
            throw new RuntimeException("User with email " + email + " not found");
        }
    }


    public void approveUser(String userEmail) {
        User user = repository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        user.setIsApproved(true);

        String generatedPassword = generateRandomPassword();

        String encodedPassword = passwordEncoder.encode(generatedPassword);

        user.setPassword(encodedPassword);

        repository.save(user);

        emailService.sendApprovalEmail(user.getEmail(), generatedPassword);
    }


    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }
}
