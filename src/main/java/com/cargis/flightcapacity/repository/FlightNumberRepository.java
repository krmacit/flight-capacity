package com.cargis.flightcapacity.repository;

import com.cargis.flightcapacity.model.FlightNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightNumberRepository extends CrudRepository<FlightNumber, Long> {
    Optional<FlightNumber> findOneByNumber(String number);
}
