package com.polezhaiev.logistics.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class AdminLoginRequestDto {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public AdminLoginRequestDto() {
    }

    public AdminLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
