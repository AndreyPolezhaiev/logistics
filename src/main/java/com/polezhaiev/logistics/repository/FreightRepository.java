package com.polezhaiev.logistics.repository;

import com.polezhaiev.logistics.model.Freight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FreightRepository extends JpaRepository<Freight, Long> {
    Optional<Freight> findByIdAndBrokerId(Long id, Long brokerId);

    List<Freight> findAllByBrokerId(Long brokerId);
}
