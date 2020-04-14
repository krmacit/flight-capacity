package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.model.FlightNumber;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightRadarService {

    private final FlightRadarClient flightRadarClient;
    private final FlightNumberService flightNumberService;

    public ArrayList<String> getFlights() {
        String bound;
        String id;
        ArrayList<String> flightIds = new ArrayList<String>();
        for (int i = 0; i < 18; i++) {
            bound = "89.93,-89.95," + (-180 + i * 20) + ".00," + (-180 + (i + 1) * 20) + ".00";
            Iterator<String> itr = flightRadarClient.getFlights(bound).keySet().iterator();
            while (itr.hasNext()) {
                id = itr.next();
                if (!id.equals("full_count") && !id.equals("version")) flightIds.add(id);
            }

            mergeFlightNumber("TK1212");

        }
        return flightIds;
    }

    private void mergeFlightNumber(String number) {
        Optional<FlightNumber> optional = flightNumberService.findByNumber(number);
        FlightNumber flightNumber;
        if (optional.isPresent()) {
            flightNumber = optional.get();
            flightNumber.setNumber(number);
        } else {
            flightNumber = new FlightNumber();
        }

        flightNumber.setNumber(number);
        flightNumberService.save(flightNumber);
    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

}
