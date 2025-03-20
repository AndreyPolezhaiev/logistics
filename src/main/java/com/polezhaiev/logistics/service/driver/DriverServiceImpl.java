package com.polezhaiev.logistics.service.driver;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverUpdateLocationRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverUpdateLocationResponseDto;
import com.polezhaiev.logistics.exception.EntityNotFoundException;
import com.polezhaiev.logistics.mapper.DriverMapper;
import com.polezhaiev.logistics.model.Driver;
import com.polezhaiev.logistics.repository.DriverRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

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
        updatedDriver.setLocation(requestDto.getLocation().trim());

        Driver savedDriver = driverRepository.save(updatedDriver);
        return driverMapper.toDto(savedDriver);
    }

    @Override
    public DriverUpdateLocationResponseDto updateLocation(Long id, DriverUpdateLocationRequestDto requestDto) {
        Driver driver = driverRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Driver by id: " + id + ", doesn't exist")
        );

        driver.setLocation(requestDto.getLocation());
        Driver updatedDriver = driverRepository.save(driver);

        return new DriverUpdateLocationResponseDto()
                .setEmail(updatedDriver.getEmail())
                .setLocation(updatedDriver.getLocation())
                .setName(updatedDriver.getName())
                .setRate(updatedDriver.getRate())
                .setTruck(updatedDriver.getTruck())
                .setId(updatedDriver.getId())
                .setPhoneNumber(updatedDriver.getPhoneNumber());
    }

    @Override
    public String deleteById(Long id) {
        driverRepository.deleteById(id);
        return "Driver by id: " + id + " deleted";
    }

    @Override
    public List<DriverResponseDto> findByLocation(String location) {
        return driverRepository.findByLocation(location)
                .stream()
                .map(driverMapper::toDto)
                .toList();
    }
}
