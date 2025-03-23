package com.polezhaiev.logistics.service.driver;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverUpdateLocationRequestDto;
import com.polezhaiev.logistics.dto.driver.LocationResponseDto;
import com.polezhaiev.logistics.exception.EntityNotFoundException;
import com.polezhaiev.logistics.mapper.DriverMapper;
import com.polezhaiev.logistics.model.Driver;
import com.polezhaiev.logistics.repository.DriverRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.polezhaiev.logistics.service.driver.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final LocationService locationService;

    private static final double EARTH_RADIUS = 6371;

    @Override
    public List<DriverResponseDto> findAll() {
        return driverRepository.findAll()
                .stream()
                .map(driverMapper::toDto)
                .toList();
    }

    @Override
    public DriverResponseDto findById(Long id) {
        Driver driver = driverRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find the driver by id: " + id)
        );
        return driverMapper.toDto(driver);
    }

    @Override
    public DriverResponseDto update(DriverRequestDto requestDto) {
        Driver driver = driverRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Driver by email: " + requestDto.getEmail() + ", doesn't exist")
        );

        Driver updatedDriver = driverMapper.toModel(requestDto);
        updatedDriver.setId(driver.getId());
        updatedDriver.setLocation(requestDto.getLocation().replace(",", " ").trim());

        Driver savedDriver = driverRepository.save(updatedDriver);
        return driverMapper.toDto(savedDriver);
    }

    @Override
    public DriverResponseDto updateLocation(Long id, DriverUpdateLocationRequestDto requestDto) {
        Driver driver = driverRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Driver by id: " + id + ", doesn't exist")
        );

        driver.setLocation(requestDto.getLocation().replace(",", " ").trim());
        Driver updatedDriver = driverRepository.save(driver);

        return driverMapper.toDto(updatedDriver);
    }

    @Override
    public String deleteById(Long id) {
        driverRepository.deleteById(id);
        return "Driver by id: " + id + " deleted";
    }

    @Override
    public List<DriverResponseDto> findDriversNearby(String location, double radiusInKm) {
        LocationResponseDto locData = locationService.getCoordinates(location)
                .orElseThrow(() -> new RuntimeException("Location not found: " + location));

        System.out.println("Found location: " + locData.getDisplayName() +
                " (" + locData.getLatitude() + ", " + locData.getLongitude() + ")");

        return driverRepository.findAll().stream()
                .map(driver -> {
                    LocationResponseDto driverCoords = locationService.getCoordinates(driver.getLocation())
                            .orElseThrow(() -> new RuntimeException("Invalid location: " + driver.getLocation()));

                    double distance = haversine(locData.getLatitude(), locData.getLongitude(),
                            driverCoords.getLatitude(), driverCoords.getLongitude());

                    System.out.println("Distance from " + locData.getDisplayName() +
                            " to " + driver.getLocation() + " is " + distance + " km");

                    return distance <= radiusInKm ? driverMapper.toDto(driver) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
