package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.model.FlightNumber;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightRadarService {

    private final FlightRadarClient flightRadarClient;
    private final FlightNumberService flightNumberService;

    public String getFlights() {
        String bound;
        String result;
        JSONObject jsonObject;
        Iterator<String> keys;

        String key;
        String latitude = "89.93,-89.95,";
        for (int i = 0; i < 18; i++) {
            bound = latitude + (-180 + i * 20) + ".00," + (-180 + (i + 1) * 20) + ".00";
            jsonObject = flightRadarClient.getFlights(bound);
            keys = jsonObject.keySet().iterator();
            for (; keys.hasNext(); ) {
                key = keys.next().toString();
                if (!key.equals("full_count") && !key.equals("version")) {
                    String[] values = jsonObject.get(key).toString().split(",");
                    if (!values[11].equals("0") && !values[12].equals("0") && values[13].length() > 2 && values[13].length() < 7) {
                        System.out.println(String.join(",", values));
                        mergeFlightNumber(values[13]);
                    }
                }
            }
        }
        return "Flight Numbers process completed.";
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
