package com.polezhaiev.logistics.dto;

import lombok.Data;

@Data
public class DriverResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String truck;
    private Double rate;
    private String documents;
}
