package com.polezhaiev.logistics.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponseDto {
    private String accessToken;
}
