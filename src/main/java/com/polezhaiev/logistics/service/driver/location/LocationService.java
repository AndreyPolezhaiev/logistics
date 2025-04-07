package com.polezhaiev.logistics.service.driver.location;


import com.polezhaiev.logistics.dto.driver.LocationResponseDto;
import reactor.core.publisher.Mono;

public interface LocationService {
    Mono<LocationResponseDto> getCoordinates(String location);
}
