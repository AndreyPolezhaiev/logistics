package com.polezhaiev.logistics.dto.freight;

import lombok.Data;

@Data
public class FreightResponseDto {
    private Long id;
    private Long brokerId;
    private String pickUpAddress;
    private String deliveryAddress;
    private Integer milesLoaded;
    private Integer milesEmpty;
    private Integer totalMiles;
    private Integer rate;
}
