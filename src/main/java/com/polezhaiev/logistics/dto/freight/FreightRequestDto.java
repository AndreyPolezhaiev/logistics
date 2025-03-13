package com.polezhaiev.logistics.dto.freight;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FreightRequestDto {
    @NotNull(message = "Pick up address is required")
    private String pickUpAddress;

    @NotNull(message = "Delivery address is required")
    private String deliveryAddress;
    @NotNull
    private Integer milesLoaded;
    @NotNull
    private Integer milesEmpty;
    @NotNull
    private Integer rate;
}
