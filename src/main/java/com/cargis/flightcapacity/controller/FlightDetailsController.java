package com.cargis.flightcapacity.controller;

import com.cargis.flightcapacity.model.FlightDetails;
import com.cargis.flightcapacity.service.FlightDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = FlightDetailsController.ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class FlightDetailsController {

    public static final String ENDPOINT = "flight-details";

    private final FlightDetailService flightDetailService;


    @PostMapping(value = "/create")
    public ResponseEntity<FlightDetails> create(@RequestBody FlightDetails flightDetails) {
        return ResponseEntity.ok(flightDetailService.save(flightDetails));
    }

    @GetMapping
    public ResponseEntity<List<FlightDetails>> findAll() {
        return ResponseEntity.ok(flightDetailService.findAll());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

}