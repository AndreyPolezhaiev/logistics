package com.polezhaiev.logistics.service.auth.admin;

import com.polezhaiev.logistics.dto.admin.AdminLoginRequestDto;
import com.polezhaiev.logistics.dto.admin.AdminLoginResponseDto;
import com.polezhaiev.logistics.dto.admin.AdminRequestDto;
import com.polezhaiev.logistics.dto.admin.AdminResponseDto;

public interface AdminAuthService {
    AdminResponseDto registerAdmin(AdminRequestDto adminRequestDto);
    AdminLoginResponseDto authenticateAdmin(AdminLoginRequestDto loginRequestDto);
}
