package com.polezhaiev.logistics.service.dispatcher;

import com.polezhaiev.logistics.dto.dispather.DispatcherRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherResponseDto;
import java.util.List;

public interface DispatcherService {
    DispatcherResponseDto save(DispatcherRequestDto requestDto);

    List<DispatcherResponseDto> findAll();

    DispatcherResponseDto findById(Long id);

    DispatcherResponseDto update(DispatcherRequestDto requestDto);

    String deleteById(Long id);
}
