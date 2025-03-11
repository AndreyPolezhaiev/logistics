package com.polezhaiev.logistics.service.auth.broker;

import com.polezhaiev.logistics.dto.broker.BrokerLoginRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerLoginResponseDto;
import com.polezhaiev.logistics.dto.broker.BrokerRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerResponseDto;

public interface BrokerAuthService {
    BrokerResponseDto registerBroker(BrokerRequestDto requestDto);
    BrokerLoginResponseDto authenticateBroker(BrokerLoginRequestDto loginRequestDto);
}
