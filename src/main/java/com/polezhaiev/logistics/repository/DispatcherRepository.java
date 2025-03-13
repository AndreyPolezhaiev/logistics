package com.polezhaiev.logistics.repository;

import com.polezhaiev.logistics.model.Dispatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DispatcherRepository extends JpaRepository<Dispatcher, Long> {
    Optional<Dispatcher> findByEmail(String email);
}
