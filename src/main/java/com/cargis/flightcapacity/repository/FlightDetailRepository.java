package com.cargis.flightcapacity.repository;

import com.cargis.flightcapacity.model.FlightDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface FlightDetailRepository extends CrudRepository<FlightDetail, Long> {
    Optional<FlightDetail> findByFlightNumberAndActualDepartureTime(String flightNumber, Date actualDepartureTime);
    Optional<FlightDetail> findByFlightRadarDetailID(Long flightRadarDetailID);
}
