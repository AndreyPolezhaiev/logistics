package com.polezhaiev.logistics.service.freight;

import com.polezhaiev.logistics.dto.freight.FreightRequestDto;
import com.polezhaiev.logistics.dto.freight.FreightResponseDto;
import com.polezhaiev.logistics.exception.EntityNotFoundException;
import com.polezhaiev.logistics.mapper.FreightMapper;
import com.polezhaiev.logistics.model.Broker;
import com.polezhaiev.logistics.model.Freight;
import com.polezhaiev.logistics.repository.BrokerRepository;
import com.polezhaiev.logistics.repository.FreightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreightServiceImpl implements FreightService {
    private final FreightRepository freightRepository;
    private final BrokerRepository brokerRepository;
    private final FreightMapper freightMapper;

    @Override
    public FreightResponseDto addFreight(Long brokerId, FreightRequestDto requestDto) {
        Broker broker = brokerRepository.findById(brokerId)
                .orElseThrow(() -> new RuntimeException("Broker not found with id: " + brokerId));

        Freight freight = freightMapper.toModel(requestDto);
        freight.setBroker(broker);
        freight.setTotalMiles(requestDto.getMilesLoaded() + requestDto.getMilesEmpty());

        Freight savedFreight = freightRepository.save(freight);

        return freightMapper.toDto(savedFreight);
    }

    @Override
    public List<FreightResponseDto> findAll(Long brokerId) {
        getBrokerOrThrow(brokerId);

        return freightRepository.findAllByBrokerId(brokerId)
                .stream()
                .map(freightMapper::toDto)
                .toList();
    }

    @Override
    public FreightResponseDto findById(Long brokerId, Long freightId) {
        Freight freight = getFreightByBroker(freightId, brokerId);
        return freightMapper.toDto(freight);
    }

    @Override
    public FreightResponseDto update(Long brokerId, Long freightId, FreightRequestDto requestDto) {
        Freight existingFreight = getFreightByBroker(freightId, brokerId);

        freightMapper.updateModelFromDto(requestDto, existingFreight);

        existingFreight.setTotalMiles(requestDto.getMilesLoaded() + requestDto.getMilesEmpty());

        Freight savedFreight = freightRepository.save(existingFreight);

        return freightMapper.toDto(savedFreight);
    }

    @Override
    public String deleteById(Long brokerId, Long freightId) {
        Freight freight = getFreightByBroker(freightId, brokerId);
        freightRepository.delete(freight);
        return "Freight with id: " + freightId + " deleted for broker id: " + brokerId;
    }

    private void getBrokerOrThrow(Long brokerId) {
        brokerRepository.findById(brokerId)
                .orElseThrow(() -> new EntityNotFoundException("Broker not found with id: " + brokerId));
    }

    private Freight getFreightByBroker(Long freightId, Long brokerId) {
        return freightRepository.findByIdAndBrokerId(freightId, brokerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Freight not found for broker " + brokerId + " and freight " + freightId));
    }
}
