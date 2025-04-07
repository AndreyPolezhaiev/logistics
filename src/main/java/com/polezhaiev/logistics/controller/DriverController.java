package com.polezhaiev.logistics.controller;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverUpdateLocationRequestDto;
import com.polezhaiev.logistics.service.driver.DriverService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/driver")
public class DriverController {
    private final DriverService driverService;

    @GetMapping("/getAll")
    public List<DriverResponseDto> findAll() {
        return driverService.findAll();
    }

    @GetMapping("/{id}")
    public DriverResponseDto findById(@PathVariable Long id) {
        return driverService.findById(id);
    }

    @GetMapping("/search")
    public Flux<DriverResponseDto> findDriversByLocation(
            @RequestParam(value = "location") String location,
            @RequestParam(defaultValue = "50") double radius) {
        return driverService.findDriversNearby(location, radius);
    }

    @PutMapping
    public DriverResponseDto update(@RequestBody @Valid DriverRequestDto requestDto) {
        return driverService.update(requestDto);
    }

    @PutMapping("/location/{id}")
    public DriverResponseDto updateLocation(
            @PathVariable Long id, @RequestBody @Valid DriverUpdateLocationRequestDto requestDto) {
        return driverService.updateLocation(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id) {
        return driverService.deleteById(id);
    }
}
