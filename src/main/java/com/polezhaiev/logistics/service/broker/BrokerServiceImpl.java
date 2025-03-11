package com.polezhaiev.logistics.service.broker;

import com.polezhaiev.logistics.dto.broker.BrokerRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerResponseDto;
import com.polezhaiev.logistics.exception.EntityNotFoundException;
import com.polezhaiev.logistics.mapper.BrokerMapper;
import com.polezhaiev.logistics.model.Broker;
import com.polezhaiev.logistics.repository.broker.BrokerRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BrokerServiceImpl implements BrokerService {
    private final BrokerRepository brokerRepository;
    private final BrokerMapper brokerMapper;

    @Override
    public List<BrokerResponseDto> findAll() {
        return brokerRepository.findAll()
                .stream()
                .map(brokerMapper::toDto)
                .toList();
    }

    @Override
    public BrokerResponseDto findById(Long id) {
        Broker broker = brokerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find broker by id: " + id)
        );
        return brokerMapper.toDto(broker);
    }

    @Override
    public BrokerResponseDto update(BrokerRequestDto requestDto) {
        Broker broker = brokerRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Dispatcher by email: " + requestDto.getEmail() + ", doesn't exist"));

        Broker updatedBroker = brokerMapper.toModel(requestDto);
        updatedBroker.setId(broker.getId());

        Broker savedBroker = brokerRepository.save(updatedBroker);
        return brokerMapper.toDto(savedBroker);
    }

    @Override
    public String deleteById(Long id) {
        brokerRepository.deleteById(id);
        return "Broker by id: " + id + " deleted";
    }
}
