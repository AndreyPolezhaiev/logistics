package com.polezhaiev.logistics.service.driver.location;


import com.polezhaiev.logistics.dto.driver.LocationResponseDto;

import java.util.Optional;

public interface LocationService {
    Optional<LocationResponseDto> getCoordinates(String location);
}
