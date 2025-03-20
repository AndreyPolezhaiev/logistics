package com.polezhaiev.logistics.dto.driver;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverUpdateLocationRequestDto {
    @NotNull
    private String location;
}
