package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.model.FlightDetail;
import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.repository.FlightDetailRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightDetailService {

    private final FlightDetailRepository flightDetailRepository;

    public FlightDetail save(FlightDetail flightDetail) {
        return flightDetailRepository.save(flightDetail);
    }

    public List<FlightDetail> findAll() {
        List<FlightDetail> flightDetails = new ArrayList<>();
        flightDetailRepository.findAll().forEach(flightDetails::add);
        return flightDetails;
    }

    public Optional<FlightDetail> findById(Long id) {
        return flightDetailRepository.findById(id);
    }

    public Optional<FlightDetail> findByFlightNumberAndActualDepartureTime(String flightNumber, Date actualDepartureTime) {
        return flightDetailRepository.findByFlightNumberAndActualDepartureTime(flightNumber, actualDepartureTime);
    }

    public void delete(Long id) {
        flightDetailRepository.deleteById(id);
    }
}