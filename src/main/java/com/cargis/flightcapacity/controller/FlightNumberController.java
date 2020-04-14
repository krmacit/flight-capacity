package com.cargis.flightcapacity.controller;

import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.service.FlightNumberService;
import feign.Body;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = FlightNumberController.ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FlightNumberController {

    public static final String ENDPOINT = "flight-numbers";

    @Autowired
    private FlightNumberService flightNumberService;

    @PostMapping(value = "/create")
    public ResponseEntity<FlightNumber> create(@RequestBody FlightNumber flightNumber){
        log.info(flightNumber.toString());
        return ResponseEntity.ok(flightNumberService.create(flightNumber));
    }

    @GetMapping
    public ResponseEntity<List<FlightNumber>> findAll(){
        return ResponseEntity.ok(flightNumberService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<FlightNumber>> findById(@PathVariable String id){
        Long longId = Long.parseLong(id);
        return ResponseEntity.ok(flightNumberService.findByID(longId));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        Long longId = Long.parseLong(id);
        return ResponseEntity.ok(flightNumberService.delete(longId));
    }

}
