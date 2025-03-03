package com.polezhaiev.logistics.mapper;

import com.polezhaiev.logistics.config.MapperConfig;
import com.polezhaiev.logistics.dto.admin.AdminLoginRequestDto;
import com.polezhaiev.logistics.dto.admin.AdminRequestDto;
import com.polezhaiev.logistics.dto.admin.AdminResponseDto;
import com.polezhaiev.logistics.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AdminMapper {
    AdminResponseDto toDto(Admin admin);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Admin toModel(AdminRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Admin toModel(AdminLoginRequestDto requestDto);
}
