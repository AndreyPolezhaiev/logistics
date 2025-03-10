package com.polezhaiev.logistics.service.auth.driver;

import com.polezhaiev.logistics.dto.driver.DriverLoginRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverLoginResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;

public interface DriverAuthService {
    DriverResponseDto registerDriver(DriverRequestDto driverRequestDto);
    DriverLoginResponseDto authenticateDriver(DriverLoginRequestDto loginRequestDto);
}
