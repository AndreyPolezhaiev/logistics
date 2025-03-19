package com.polezhaiev.logistics.mapper;

import com.polezhaiev.logistics.config.MapperConfig;
import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface DriverMapper {
    DriverResponseDto toDto(Driver driver);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "password", target = "password")
    Driver toModel(DriverRequestDto requestDto);
}
