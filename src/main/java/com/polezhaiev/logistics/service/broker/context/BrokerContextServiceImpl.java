package com.polezhaiev.logistics.service.broker.context;

import com.polezhaiev.logistics.exception.EntityNotFoundException;
import com.polezhaiev.logistics.model.Broker;
import com.polezhaiev.logistics.repository.BrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrokerContextServiceImpl implements BrokerContextService {

    private final BrokerRepository brokerRepository;

    @Override
    public Broker getCurrentBroker(Jwt jwt) {
        String sub = jwt.getClaim("sub");
        return brokerRepository.findByCognitoSub(sub)
                .orElseThrow(() -> new EntityNotFoundException("Broker with sub " + sub + " not found"));
    }

    @Override
    public Long getCurrentBrokerId(Jwt jwt) {
        return getCurrentBroker(jwt).getId();
    }
}
