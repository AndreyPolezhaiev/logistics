package com.polezhaiev.logistics.service.driver;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverUpdateLocationRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DriverService {
    List<DriverResponseDto> findAll();

    DriverResponseDto findById(Long id);

    DriverResponseDto update(DriverRequestDto requestDto);
    DriverResponseDto updateLocation(Long id, DriverUpdateLocationRequestDto requestDto);

    String deleteById(Long id);

    Flux<DriverResponseDto> findDriversNearby(String location, double radiusInKm);
}
