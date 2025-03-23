package com.polezhaiev.logistics.dto.driver;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationResponseDto {
    private double latitude;
    private double longitude;
    private String displayName;
}
