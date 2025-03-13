package com.polezhaiev.logistics.service.broker.context;

import com.polezhaiev.logistics.model.Broker;
import org.springframework.security.oauth2.jwt.Jwt;

public interface BrokerContextService {
    Broker getCurrentBroker(Jwt jwt);

    Long getCurrentBrokerId(Jwt jwt);
}
