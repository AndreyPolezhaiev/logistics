package com.polezhaiev.logistics.controller;

import com.polezhaiev.logistics.dto.broker.BrokerRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerResponseDto;
import com.polezhaiev.logistics.service.broker.BrokerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/broker")
@AllArgsConstructor
public class BrokerController {
    private final BrokerService brokerService;

    @GetMapping("/getAll")
    public List<BrokerResponseDto> findAll() {
        return brokerService.findAll();
    }

    @GetMapping("/{id}")
    public BrokerResponseDto findById(@PathVariable Long id) {
        return brokerService.findById(id);
    }

    @PutMapping
    public BrokerResponseDto update(@RequestBody @Valid BrokerRequestDto requestDto) {
        return brokerService.update(requestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id) {
        return brokerService.deleteById(id);
    }
}
