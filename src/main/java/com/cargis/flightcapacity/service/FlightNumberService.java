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

    public FlightNumber save(FlightNumber flightNumber) {
        return flightNumberRepository.save(flightNumber);
    }

    public List<FlightNumber> findAll() {
        List<FlightNumber> flightNumbers = new ArrayList<>();
        flightNumberRepository.findAll().forEach(flightNumbers::add);
        return flightNumbers;
    }

    public Optional<FlightNumber> findById(Long id) {
        return flightNumberRepository.findById(id);
    }

    public Optional<FlightNumber> findByNumber(String number) {
        return flightNumberRepository.findByNumber(number);
    }

    public void delete(Long id) {
        flightNumberRepository.deleteById(id);
    }
}
