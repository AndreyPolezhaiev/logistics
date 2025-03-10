package com.polezhaiev.logistics.service.driver;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;

import java.util.List;

public interface DriverService {
    List<DriverResponseDto> findAll();

    DriverResponseDto findById(Long id);

    DriverResponseDto update(DriverRequestDto requestDto);

    String deleteById(Long id);
}
