package com.polezhaiev.logistics.dto.driver;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverRequestDto {
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String truck;
    @NotNull
    private String location;
    private Double rate;
}
