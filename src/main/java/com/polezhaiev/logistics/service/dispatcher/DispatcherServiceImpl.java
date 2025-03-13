package com.polezhaiev.logistics.service.dispatcher;

import com.polezhaiev.logistics.dto.dispather.DispatcherRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherResponseDto;
import com.polezhaiev.logistics.exception.EntityNotFoundException;
import com.polezhaiev.logistics.mapper.DispatcherMapper;
import com.polezhaiev.logistics.model.Dispatcher;
import com.polezhaiev.logistics.repository.DispatcherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatcherServiceImpl implements DispatcherService {
    private final DispatcherRepository dispatcherRepository;
    private final DispatcherMapper dispatcherMapper;

    @Override
    public List<DispatcherResponseDto> findAll() {
        return dispatcherRepository.findAll()
                .stream()
                .map(dispatcherMapper::toDto)
                .toList();
    }

    @Override
    public DispatcherResponseDto findById(Long id) {
        Dispatcher dispatcher = dispatcherRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find dispatcher by id: " + id)
        );
        return dispatcherMapper.toDto(dispatcher);
    }

    @Override
    public DispatcherResponseDto update(DispatcherRequestDto requestDto) {
        Dispatcher dispatcher = dispatcherRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Dispatcher by email: " + requestDto.getEmail() + ", doesn't exist"));

        Dispatcher updatedDispatcher = dispatcherMapper.toModel(requestDto);
        updatedDispatcher.setId(dispatcher.getId());

        Dispatcher savedDispatcher = dispatcherRepository.save(updatedDispatcher);
        return dispatcherMapper.toDto(savedDispatcher);
    }

    @Override
    public String deleteById(Long id) {
        dispatcherRepository.deleteById(id);
        return "Dispatcher by id: " + id + " deleted";
    }
}
