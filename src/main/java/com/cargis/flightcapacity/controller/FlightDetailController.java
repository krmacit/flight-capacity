package com.cargis.flightcapacity.controller;

import com.cargis.flightcapacity.model.FlightDetail;
import com.cargis.flightcapacity.service.FlightDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = FlightDetailController.ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class FlightDetailController {

    public static final String ENDPOINT = "flight-details";

    private final FlightDetailService flightDetailService;


    @PostMapping(value = "/create")
    public ResponseEntity<FlightDetail> create(@RequestBody FlightDetail flightDetail) {
        return ResponseEntity.ok(flightDetailService.save(flightDetail));
    }

    @GetMapping
    public ResponseEntity<List<FlightDetail>> findAll() {
        return ResponseEntity.ok(flightDetailService.findAll());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

}