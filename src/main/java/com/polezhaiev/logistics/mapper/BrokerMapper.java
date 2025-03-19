package com.polezhaiev.logistics.mapper;

import com.polezhaiev.logistics.config.MapperConfig;
import com.polezhaiev.logistics.dto.broker.BrokerRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerResponseDto;
import com.polezhaiev.logistics.model.Broker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BrokerMapper {
    BrokerResponseDto toDto(Broker broker);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freights", ignore = true)
    @Mapping(source = "password", target = "password")
    @Mapping(target = "cognitoSub", ignore = true)
    Broker toModel(BrokerRequestDto requestDto);
}
