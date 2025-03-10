package com.polezhaiev.logistics.service.auth.dispatcher;

import com.polezhaiev.logistics.dto.dispather.DispatcherLoginRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherLoginResponseDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherResponseDto;

public interface DispatcherAuthService {
    DispatcherResponseDto registerDispatcher(DispatcherRequestDto dispatcherRequestDto);
    DispatcherLoginResponseDto authenticateDispatcher(DispatcherLoginRequestDto loginRequestDto);
}
