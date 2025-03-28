package com.polezhaiev.logistics.dto.driver;

import lombok.Data;

@Data
public class DriverResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String truck;
    private String location;
    private Double rate;
}
