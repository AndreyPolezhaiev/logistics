package com.polezhaiev.logistics.controller;

import com.polezhaiev.logistics.dto.admin.*;
import com.polezhaiev.logistics.service.auth.admin.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    // ✅ Регистрация администратора
    @PostMapping("/register-admin")
    public ResponseEntity<AdminResponseDto> registerAdmin(@Valid @RequestBody AdminRequestDto request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    // ✅ Логин администратора (получение JWT-токена)
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@Valid @RequestBody AdminLoginRequestDto loginRequest) {
        return ResponseEntity.ok(authService.authenticateAdmin(loginRequest));
    }
}
