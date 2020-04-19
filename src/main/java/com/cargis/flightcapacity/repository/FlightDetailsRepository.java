package com.cargis.flightcapacity.repository;

import com.cargis.flightcapacity.model.FlightDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightDetailsRepository extends CrudRepository<FlightDetails, Long> {
}
