package com.polezhaiev.logistics.dto.dispather;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DispatcherRequestDto {
    @NotNull
    private String name;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    @NotNull
    private String company;
    @NotNull
    private Long mc;
    @NotNull
    private String phoneNumber;
    private Integer rate;
}
