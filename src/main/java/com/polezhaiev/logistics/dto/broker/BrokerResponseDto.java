package com.polezhaiev.logistics.dto.broker;

import lombok.Data;

@Data
public class BrokerResponseDto {
    private String id;
    private String company;
    private String phoneNumber;
    private String email;
    private String mc;
}
