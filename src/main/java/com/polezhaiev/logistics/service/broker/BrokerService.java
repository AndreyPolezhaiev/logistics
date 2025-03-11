package com.polezhaiev.logistics.service.broker;

import com.polezhaiev.logistics.dto.broker.BrokerRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerResponseDto;
import java.util.List;

public interface BrokerService {
    List<BrokerResponseDto> findAll();

    BrokerResponseDto findById(Long id);

    BrokerResponseDto update(BrokerRequestDto requestDto);

    String deleteById(Long id);
}
