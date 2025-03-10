package com.polezhaiev.logistics.service.driver;

import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.exception.EntityNotFoundException;
import com.polezhaiev.logistics.mapper.DriverMapper;
import com.polezhaiev.logistics.model.Driver;
import com.polezhaiev.logistics.repository.driver.DriverRepository;
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

        Driver savedUpdatedDriver = driverRepository.save(updatedDriver);
        return driverMapper.toDto(savedUpdatedDriver);
    }

    @Override
    public String deleteById(Long id) {
        driverRepository.deleteById(id);
        return "Driver by id: " + id + " deleted";
    }
}
