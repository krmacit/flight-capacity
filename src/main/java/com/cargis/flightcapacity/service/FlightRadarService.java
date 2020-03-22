package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightRadarService {

    private final FlightRadarClient flightRadarClient;

    public JSONObject getFlights() {
        return flightRadarClient.getFlights();
    }

}
