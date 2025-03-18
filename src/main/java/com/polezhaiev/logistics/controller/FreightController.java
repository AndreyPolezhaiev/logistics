package com.polezhaiev.logistics.controller;

import com.polezhaiev.logistics.dto.freight.FreightRequestDto;
import com.polezhaiev.logistics.dto.freight.FreightResponseDto;
import com.polezhaiev.logistics.service.broker.context.BrokerContextService;
import com.polezhaiev.logistics.service.freight.FreightService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freight")
@RequiredArgsConstructor
public class FreightController {

    private final FreightService freightService;
    private final BrokerContextService brokerContextService;

    @Operation(summary = "Add a new freight", description = "Add a new freight for authenticated broker")
    @PostMapping("/")
    public ResponseEntity<FreightResponseDto> addFreight(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid FreightRequestDto requestDto) {

        Long brokerId = brokerContextService.getCurrentBrokerId(jwt);
        FreightResponseDto responseDto = freightService.addFreight(brokerId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get all freights", description = "Get all freights for authenticated broker")
    @GetMapping
    public ResponseEntity<List<FreightResponseDto>> getAll(@AuthenticationPrincipal Jwt jwt) {
        Long brokerId = brokerContextService.getCurrentBrokerId(jwt);
        List<FreightResponseDto> freights = freightService.findAll(brokerId);
        return ResponseEntity.ok(freights);
    }

    @Operation(summary = "Get freight by ID", description = "Get specific freight by ID for authenticated broker")
    @GetMapping("/{freightId}")
    public ResponseEntity<FreightResponseDto> getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long freightId) {

        Long brokerId = brokerContextService.getCurrentBrokerId(jwt);
        FreightResponseDto responseDto = freightService.findById(brokerId, freightId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Update freight", description = "Update existing freight for authenticated broker")
    @PutMapping("/{freightId}")
    public ResponseEntity<FreightResponseDto> update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long freightId,
            @RequestBody @Valid FreightRequestDto requestDto) {

        Long brokerId = brokerContextService.getCurrentBrokerId(jwt);
        FreightResponseDto updatedFreight = freightService.update(brokerId, freightId, requestDto);
        return ResponseEntity.ok(updatedFreight);
    }

    @Operation(summary = "Delete freight", description = "Delete freight by ID for authenticated broker")
    @DeleteMapping("/{freightId}")
    public ResponseEntity<String> deleteById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long freightId) {

        Long brokerId = brokerContextService.getCurrentBrokerId(jwt);
        String result = freightService.deleteById(brokerId, freightId);
        return ResponseEntity.ok(result);
    }
}
