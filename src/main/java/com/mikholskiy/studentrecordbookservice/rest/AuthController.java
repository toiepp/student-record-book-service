package com.mikholskiy.studentrecordbookservice.rest;


import com.mikholskiy.studentrecordbookservice.config.CustomUserDetails;
import com.mikholskiy.studentrecordbookservice.dto.AuthLoginDTO;
import com.mikholskiy.studentrecordbookservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/register")
    public String addNewUser(@RequestBody AuthLoginDTO userCredential){
        return authService.saveUser(userCredential);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthLoginDTO authLoginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authLoginDTO.getEmail(), authLoginDTO.getPassword())
            );
            if (authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                return authService.generateToken(authLoginDTO.getEmail(), userDetails.getRole());
            } else {
                throw new RuntimeException("Authentication failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception during authentication");
        }
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody AuthLoginDTO authLoginDTO) {
        try {
            return authService.loginUser(authLoginDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception during login");
        }
    }

    @GetMapping("/validate")
    public String validate(@RequestParam String token){
        authService.validateToken(token);
        return "Token is valid";
    }



}
