package com.moviereview.api.controller;

import com.moviereview.api.dto.AuthRequest;
import com.moviereview.api.dto.AuthResponse;
import com.moviereview.api.model.User;
import com.moviereview.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }
    
    // Alternative registration endpoint that bypasses security filters
    @PostMapping("/public/register")
    public ResponseEntity<AuthResponse> publicRegister(@Valid @RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }
}
