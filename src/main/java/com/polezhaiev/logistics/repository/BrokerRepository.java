package com.polezhaiev.logistics.repository;

import com.polezhaiev.logistics.model.Broker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrokerRepository extends JpaRepository<Broker, Long> {
    Optional<Broker> findByEmail(String email);
    Optional<Broker> findByCognitoSub(String cognitoSub);
}
