package com.polezhaiev.logistics.mapper;

import com.polezhaiev.logistics.config.MapperConfig;
import com.polezhaiev.logistics.dto.freight.FreightRequestDto;
import com.polezhaiev.logistics.dto.freight.FreightResponseDto;
import com.polezhaiev.logistics.model.Freight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class)
public interface FreightMapper {
    @Mapping(source = "broker.id", target = "brokerId")
    FreightResponseDto toDto(Freight freight);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "broker", ignore = true),
            @Mapping(target = "totalMiles", ignore = true)
    })
    Freight toModel(FreightRequestDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "broker", ignore = true),
            @Mapping(target = "totalMiles", ignore = true)
    })
    void updateModelFromDto(FreightRequestDto dto, @MappingTarget Freight freight);

}
