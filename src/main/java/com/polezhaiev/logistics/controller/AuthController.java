package com.polezhaiev.logistics.controller;

import com.polezhaiev.logistics.dto.admin.*;
import com.polezhaiev.logistics.dto.broker.BrokerLoginRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerLoginResponseDto;
import com.polezhaiev.logistics.dto.broker.BrokerRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerResponseDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherLoginRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherLoginResponseDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverLoginRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverLoginResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.service.auth.admin.AdminAuthService;
import com.polezhaiev.logistics.service.auth.broker.BrokerAuthService;
import com.polezhaiev.logistics.service.auth.dispatcher.DispatcherAuthService;
import com.polezhaiev.logistics.service.auth.driver.DriverAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AdminAuthService adminAuthService;
    private final DispatcherAuthService dispatcherAuthService;
    private final DriverAuthService driverAuthService;
    private final BrokerAuthService brokerAuthService;

    // ✅ Регистрация администратора
    @PostMapping("/register-admin")
    public ResponseEntity<AdminResponseDto> registerAdmin(@Valid @RequestBody AdminRequestDto request) {
        return ResponseEntity.ok(adminAuthService.registerAdmin(request));
    }

    // ✅ Логин администратора (получение JWT-токена)
    @PostMapping("/login-admin")
    public ResponseEntity<AdminLoginResponseDto> loginAdmin(@Valid @RequestBody AdminLoginRequestDto loginRequest) {
        return ResponseEntity.ok(adminAuthService.authenticateAdmin(loginRequest));
    }

    // ✅ Регистрация диспетчера
    @PostMapping("/register-dispatcher")
    public ResponseEntity<DispatcherResponseDto> registerDispatcher(@Valid @RequestBody DispatcherRequestDto request) {
        return ResponseEntity.ok(dispatcherAuthService.registerDispatcher(request));
    }

    // ✅ Логин диспетчера (получение JWT-токена)
    @PostMapping("/login-dispatcher")
    public ResponseEntity<DispatcherLoginResponseDto> loginDispatcher(@Valid @RequestBody DispatcherLoginRequestDto loginRequest) {
        return ResponseEntity.ok(dispatcherAuthService.authenticateDispatcher(loginRequest));
    }

    // ✅ Регистрация драйвера
    @PostMapping("/register-driver")
    public ResponseEntity<DriverResponseDto> registerDriver(@Valid @RequestBody DriverRequestDto request) {
        return ResponseEntity.ok(driverAuthService.registerDriver(request));
    }

    // ✅ Логин драйвера (получение JWT-токена)
    @PostMapping("/login-driver")
    public ResponseEntity<DriverLoginResponseDto> loginDriver(@Valid @RequestBody DriverLoginRequestDto loginRequest) {
        return ResponseEntity.ok(driverAuthService.authenticateDriver(loginRequest));
    }

    @PostMapping("/register-broker")
    public ResponseEntity<BrokerResponseDto> registerBroker(@Valid @RequestBody BrokerRequestDto request) {
        return ResponseEntity.ok(brokerAuthService.registerBroker(request));
    }

    // ✅ Логин драйвера (получение JWT-токена)
    @PostMapping("/login-broker")
    public ResponseEntity<BrokerLoginResponseDto> loginBroker(@Valid @RequestBody BrokerLoginRequestDto loginRequest) {
        return ResponseEntity.ok(brokerAuthService.authenticateBroker(loginRequest));
    }
}
