package com.polezhaiev.logistics.dto.driver;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class DriverUpdateLocationResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String truck;
    private String location;
    private BigDecimal rate;
}
