package com.polezhaiev.logistics.service.driver;

import com.polezhaiev.logistics.dto.DriverRequestDto;
import com.polezhaiev.logistics.dto.DriverResponseDto;

import java.util.List;

public interface DriverService {
    DriverResponseDto save(DriverRequestDto requestDto);

    List<DriverResponseDto> findAll();

    DriverResponseDto findById(Long id);

    DriverResponseDto update(DriverRequestDto requestDto);

    String delete(Long id);
}
