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

    @PostMapping("/register-admin")
    public ResponseEntity<AdminResponseDto> registerAdmin(@Valid @RequestBody AdminRequestDto requestDto) {
        return ResponseEntity.ok(adminAuthService.registerAdmin(requestDto));
    }

    @PostMapping("/login-admin")
    public ResponseEntity<AdminLoginResponseDto> loginAdmin(@Valid @RequestBody AdminLoginRequestDto requestDto) {
        return ResponseEntity.ok(adminAuthService.authenticateAdmin(requestDto));
    }

    @PostMapping("/register-dispatcher")
    public ResponseEntity<DispatcherResponseDto> registerDispatcher(@Valid @RequestBody DispatcherRequestDto requestDto) {
        return ResponseEntity.ok(dispatcherAuthService.registerDispatcher(requestDto));
    }

    @PostMapping("/login-dispatcher")
    public ResponseEntity<DispatcherLoginResponseDto> loginDispatcher(@Valid @RequestBody DispatcherLoginRequestDto requestDto) {
        return ResponseEntity.ok(dispatcherAuthService.authenticateDispatcher(requestDto));
    }


    @PostMapping("/register-driver")
    public ResponseEntity<DriverResponseDto> registerDriver(@Valid @RequestBody DriverRequestDto requestDto) {
        return ResponseEntity.ok(driverAuthService.registerDriver(requestDto));
    }

    @PostMapping("/login-driver")
    public ResponseEntity<DriverLoginResponseDto> loginDriver(@Valid @RequestBody DriverLoginRequestDto requestDto) {
        return ResponseEntity.ok(driverAuthService.authenticateDriver(requestDto));
    }

    @PostMapping("/register-broker")
    public ResponseEntity<BrokerResponseDto> registerBroker(@Valid @RequestBody BrokerRequestDto requestDto) {
        return ResponseEntity.ok(brokerAuthService.registerBroker(requestDto));
    }

    @PostMapping("/login-broker")
    public ResponseEntity<BrokerLoginResponseDto> loginBroker(@Valid @RequestBody BrokerLoginRequestDto requestDto) {
        return ResponseEntity.ok(brokerAuthService.authenticateBroker(requestDto));
    }
}
