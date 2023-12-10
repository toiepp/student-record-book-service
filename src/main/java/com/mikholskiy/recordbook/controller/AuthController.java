package com.mikholskiy.recordbook.controller;


import com.mikholskiy.recordbook.model.dto.LoginDto;
import com.mikholskiy.recordbook.model.dto.RegisterDto;
import com.mikholskiy.recordbook.model.dto.AuthResponseDto;
import com.mikholskiy.recordbook.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterDto loginDto) {
        try {
            authService.register(loginDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
