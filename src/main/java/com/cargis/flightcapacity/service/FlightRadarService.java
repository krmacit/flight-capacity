package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightRadarService {

    private final FlightRadarClient flightRadarClient;

    public ArrayList<String> getFlights() {
        String bound, id;
        ArrayList<String> flightIds = new ArrayList<String>();
        for (int i = 0 ; i < 18; i++){
            bound = "89.93,-89.95," + (-180 + i * 20) + ".00," + (-180 + (i + 1) * 20) + ".00";
            Iterator<String> itr = flightRadarClient.getFlights(bound).keySet().iterator();
            while (itr.hasNext()) {
                id = itr.next();
                if (!id.equals("full_count") && !id.equals("version")) flightIds.add(id);
            }
        }
        return flightIds;
    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

}
