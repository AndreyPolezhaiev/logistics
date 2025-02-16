package com.polezhaiev.logistics.mapper;

import com.polezhaiev.logistics.config.MapperConfig;
import com.polezhaiev.logistics.dto.dispather.DispatcherRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherResponseDto;
import com.polezhaiev.logistics.model.Dispatcher;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface DispatcherMapper {
    DispatcherResponseDto toDto(Dispatcher dispatcher);

    Dispatcher toModel(DispatcherRequestDto requestDto);
}
