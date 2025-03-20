package com.polezhaiev.logistics.service.driver;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverUpdateLocationRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverUpdateLocationResponseDto;

import java.util.List;

public interface DriverService {
    List<DriverResponseDto> findAll();

    DriverResponseDto findById(Long id);

    DriverResponseDto update(DriverRequestDto requestDto);
    DriverUpdateLocationResponseDto updateLocation(Long id, DriverUpdateLocationRequestDto requestDto);

    String deleteById(Long id);

    List<DriverResponseDto> findByLocation(String location);
}
