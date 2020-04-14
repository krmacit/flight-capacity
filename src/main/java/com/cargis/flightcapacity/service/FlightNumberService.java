package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.repository.FlightNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightNumberService {

    private final FlightNumberRepository flightNumberRepository;

    public FlightNumber create(FlightNumber flightNumber) {
        return flightNumberRepository.save(flightNumber);
    }

    public List<FlightNumber> findAll() {
        List<FlightNumber> flightNumbers = new ArrayList<>();
        flightNumberRepository.findAll().forEach(flightNumbers::add);
        return flightNumbers;
    }

    public Optional<FlightNumber> findById(Long longId) {
        return flightNumberRepository.findById(longId);
    }

    public Void delete(Long id) {
        flightNumberRepository.deleteById(id);
        return null;
    }
}
