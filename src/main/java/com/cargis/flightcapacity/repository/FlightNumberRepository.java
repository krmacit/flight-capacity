package com.cargis.flightcapacity.repository;

import com.cargis.flightcapacity.model.FlightNumber;
import org.springframework.data.repository.CrudRepository;

public interface FlightNumberRepository extends CrudRepository<FlightNumber, Long> {
}
