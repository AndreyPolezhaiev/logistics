package com.polezhaiev.logistics.dto.dispather;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DispatcherRequestDto {
    @NotNull
    private String name;
    @Email
    private String email;
    @NotNull
    private String company;
    @NotNull
    private Long mc;
    @NotNull
    private String phoneNumber;
    private Integer rate;
    private String documents;
}
