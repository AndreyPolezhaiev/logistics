package com.polezhaiev.logistics.controller;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.service.driver.DriverService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/driver")
public class DriverController {
    private final DriverService driverService;

    @PostMapping("/")
    public DriverResponseDto save(@RequestBody @Valid DriverRequestDto requestDto) {
        return driverService.save(requestDto);
    }

    @GetMapping("/getAll")
    public List<DriverResponseDto> findAll() {
        return driverService.findAll();
    }

    @GetMapping("/{id}")
    public DriverResponseDto findById(@PathVariable Long id) {
        return driverService.findById(id);
    }

    @PutMapping
    public DriverResponseDto update(@RequestBody @Valid DriverRequestDto requestDto) {
        return driverService.update(requestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id) {
        return driverService.deleteById(id);
    }
}
