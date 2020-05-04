package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarApiClient;
import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.model.FlightNumber;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightRadarService {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private final FlightRadarClient flightRadarClient;
    private final FlightRadarApiClient flightRadarApiClient;
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
            OffsetDateTime strDate = OffsetDateTime.now(ZoneId.of("UTC+03:00"));
            for (; keys.hasNext(); ) {
                key = keys.next();
                if (!key.equals("full_count") && !key.equals("version")) {
                    String[] values = jsonObject.get(key).toString().split(",");
                    if (!values[11].equals("0") && !values[12].equals("0") && values[13].length() > 2 && isNumeric(values[13].substring(3))) {
                        mergeFlightNumber(values[13], strDate);
                    }
                }
            }
        }
        return "Flight Numbers process completed.";
    }

    private void mergeFlightNumber(String number, OffsetDateTime time) {
        System.out.println(time);
        Optional<FlightNumber> optional = flightNumberService.findByNumber(number);
        FlightNumber flightNumber;
        if (optional.isPresent()) {
            flightNumber = optional.get();
        } else {
            flightNumber = new FlightNumber();
            flightNumber.setNumber(number);
            flightNumber.setCarrierCode(number.substring(0, 3));
            flightNumber.setFlightCode(Integer.parseInt(number.substring(3)));
            flightNumber.setCreatedDate(time);
        }
        flightNumber.setLastSeenDate(time);
        flightNumberService.save(flightNumber);
    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

    public JSONObject getFlightDetails(String query, String fetchBy, String limit, String page) {
        return flightRadarApiClient.getFlightDetails(query);
    }

}
