package com.polezhaiev.logistics.controller;

import com.polezhaiev.logistics.dto.dispather.DispatcherRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.service.dispatcher.DispatcherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispatcher")
public class DispatcherController {
    private final DispatcherService dispatcherService;

    @PostMapping("/")
    public DispatcherResponseDto save(@RequestBody @Valid DispatcherRequestDto requestDto) {
        return dispatcherService.save(requestDto);
    }

    @GetMapping("/getAll")
    public List<DispatcherResponseDto> findAll() {
        return dispatcherService.findAll();
    }

    @GetMapping("/{id}")
    public DispatcherResponseDto findById(@PathVariable Long id) {
        return dispatcherService.findById(id);
    }

    @PutMapping
    public DispatcherResponseDto update(@RequestBody @Valid DispatcherRequestDto requestDto) {
        return dispatcherService.update(requestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id) {
        return dispatcherService.deleteById(id);
    }
}
