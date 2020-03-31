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
        JSONObject combined = new JSONObject();
        String bound;
        for (int i = 0 ; i < 18; i++){
            bound = "89.93,-89.95," + (-180 + i * 20) + ".00," + (-180 + (i + 1) * 20) + ".00";
            combined.put(("flights " + i),flightRadarClient.getFlights(bound));
        }
        return combined;
    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

}
