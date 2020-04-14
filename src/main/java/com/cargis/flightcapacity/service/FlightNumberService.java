package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.repository.FlightNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FlightNumberService {

    @Autowired
    private FlightNumberRepository flightNumberRepository;

    public FlightNumber create(FlightNumber flightNumber) {
        return flightNumberRepository.save(flightNumber);
    }

    public FlightNumber createIfNotExits(FlightNumber flightNumber) {
        if (findAll().contains(flightNumber)){
            return flightNumber;
        }
        else {
            return flightNumberRepository.save(flightNumber);
        }
    }



    public List<FlightNumber> findAll() {
        List<FlightNumber> flightNumbers = new ArrayList<>();
        flightNumberRepository.findAll().forEach(flightNumbers::add);
        return flightNumbers;
    }

    public Optional<FlightNumber> findByID(Long longId) {
        return flightNumberRepository.findById(longId);
    }

    public Void delete(Long id) {
        flightNumberRepository.deleteById(id);
        return null;
    }
}
