package com.polezhaiev.logistics.service.freight;

import com.polezhaiev.logistics.dto.freight.FreightRequestDto;
import com.polezhaiev.logistics.dto.freight.FreightResponseDto;

import java.util.List;

public interface FreightService {
    FreightResponseDto addFreight(Long brokerId, FreightRequestDto requestDto);
    List<FreightResponseDto> findAll(Long brokerId);
    FreightResponseDto findById(Long brokerId, Long freightId);
    FreightResponseDto update(Long brokerId, Long freightId, FreightRequestDto requestDto);
    String deleteById(Long brokerId, Long freightId);
}
