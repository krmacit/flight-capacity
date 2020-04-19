package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.model.FlightDetails;
import com.cargis.flightcapacity.repository.FlightDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightDetailService {

    private final FlightDetailsRepository flightDetailsRepository;

    public FlightDetails save(FlightDetails flightDetails) {
        return flightDetailsRepository.save(flightDetails);
    }

    public List<FlightDetails> findAll() {
        List<FlightDetails> flightDetails = new ArrayList<>();
        flightDetailsRepository.findAll().forEach(flightDetails::add);
        return flightDetails;
    }

    public Optional<FlightDetails> findById(Long id) {
        return flightDetailsRepository.findById(id);
    }

    public void delete(Long id) {
        flightDetailsRepository.deleteById(id);
    }
}