package com.mikholskiy.recordbook.controller;


import com.mikholskiy.recordbook.dto.LoginDto;
import com.mikholskiy.recordbook.dto.AuthResponseDto;
import com.mikholskiy.recordbook.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController setAuthService(AuthService authService) {
        this.authService = authService;
        return this;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(authService.login(loginDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(authService.register(loginDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
