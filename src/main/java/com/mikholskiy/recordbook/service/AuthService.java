package com.mikholskiy.recordbook.service;


import com.mikholskiy.recordbook.dto.AuthResponseDto;
import com.mikholskiy.recordbook.dto.LoginDto;
import com.mikholskiy.recordbook.entity.User;
import com.mikholskiy.recordbook.entity.UserRole;
import com.mikholskiy.recordbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {
    private UserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private EmailService emailService;

    @Autowired
    public AuthService setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }

    @Autowired
    public AuthService setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
        return this;
    }

    @Autowired
    public AuthService setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    @Autowired
    public AuthService setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    @Autowired
    public AuthService setEmailService(EmailService emailService) {
        this.emailService = emailService;
        return this;
    }

    public AuthResponseDto login(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        UserDetails user = (UserDetails) userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponseDto.builder()
                .token(token)
                .build();

    }

    public AuthResponseDto register(LoginDto credential) {
        if (userRepository.findByEmail(credential.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User.UserBuilder userBuilder;

        if (credential.getUserRole() == UserRole.ADMIN) {
            userBuilder = User.builder()
                    .email(credential.getEmail())
                    .password(passwordEncoder.encode(credential.getPassword()))
                    .role(credential.getUserRole())
                    .isApproved(true);
        } else {
            userBuilder = User.builder()
                    .email(credential.getEmail())
                    .role(credential.getUserRole())
                    .createdDate(LocalDateTime.now())
                    .isApproved(false);
        }

        var user = userBuilder
                .firstName(credential.getFirstName())
                .lastName(credential.getLastName())
                .fatherName(credential.getFatherName())
                .build();


        System.out.println(jwtService.getToken(user));
        userRepository.save(user);

        return AuthResponseDto.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    public void approveUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        user.setIsApproved(true);

        String generatedPassword = generateRandomPassword();

        String encodedPassword = passwordEncoder.encode(generatedPassword);

        user.setPassword(encodedPassword);
        System.out.println(encodedPassword);
        userRepository.save(user);

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
