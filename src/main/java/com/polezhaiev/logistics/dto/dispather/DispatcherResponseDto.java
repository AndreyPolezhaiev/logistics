package com.polezhaiev.logistics.dto.dispather;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DispatcherResponseDto {
    private Long id;
    private String name;
    private Long mc;
    private String company;
    private String email;
    private String phoneNumber;
    private Integer rate;
    private String documents;
}
