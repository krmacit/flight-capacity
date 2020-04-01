package com.cargis.flightcapacity.controller;

import com.cargis.flightcapacity.service.FlightRadarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = FlightRadarController.ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class FlightRadarController {

    public static final String ENDPOINT = "flight-radar";

    private final FlightRadarService flightRadarService;

    @RequestMapping(value = "/flights")
    public ResponseEntity<ArrayList<String>> getFlights() {
        return ResponseEntity.ok(flightRadarService.getFlights());
    }

    @RequestMapping(value = "/flights/{flightId}")
    public ResponseEntity<JSONObject> getFlightDetail(@PathVariable String flightId, @RequestParam(defaultValue = "1.5") String version) {
        return ResponseEntity.ok(flightRadarService.getFlightDetail(flightId, version));
    }

}