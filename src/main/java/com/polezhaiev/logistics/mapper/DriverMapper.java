package com.polezhaiev.logistics.mapper;

import com.polezhaiev.logistics.config.MapperConfig;
import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.model.Driver;
import org.mapstruct.Mapper;


@Mapper(config = MapperConfig.class)
public interface DriverMapper {

    DriverResponseDto toDto(Driver driver);

    Driver toModel(DriverRequestDto requestDto);
}
