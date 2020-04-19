package com.cargis.flightcapacity.controller;

import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.service.FlightNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = FlightNumberController.ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class FlightNumberController {

    public static final String ENDPOINT = "flight-numbers";

    private final FlightNumberService flightNumberService;

    @PostMapping(value = "/create")
    public ResponseEntity<FlightNumber> create(@RequestBody FlightNumber flightNumber) {
        return ResponseEntity.ok(flightNumberService.save(flightNumber));
    }

    @GetMapping
    public ResponseEntity<List<FlightNumber>> findAll() {
        return ResponseEntity.ok(flightNumberService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<FlightNumber>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(flightNumberService.findById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightNumberService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
