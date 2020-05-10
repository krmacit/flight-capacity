package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarApiClient;
import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.model.FlightNumber;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static com.cargis.flightcapacity.util.DateUtils.getCurrentDate;

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
    private final FlightDetailService flightDetailService;

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
            Date currentDate = getCurrentDate();
            for (; keys.hasNext(); ) {
                key = keys.next();
                if (!key.equals("full_count") && !key.equals("version")) {
                    String[] values = jsonObject.get(key).toString().split(",");
                    if (!values[11].equals("0") && !values[12].equals("0") && values[13].length() > 2 && isNumeric(values[13].substring(3))) {
                        mergeFlightNumber(values[13], currentDate);
                    }
                }
            }
        }
        return "Flight Numbers process completed.";
    }

    private void mergeFlightNumber(String number, Date currentDate) {
        Optional<FlightNumber> optional = flightNumberService.findByNumber(number);
        FlightNumber flightNumber;
        if (optional.isPresent()) {
            flightNumber = optional.get();
        } else {
            flightNumber = new FlightNumber();
            flightNumber.setNumber(number);
            flightNumber.setCarrierCode(number.substring(0, 3));
            flightNumber.setFlightCode(Integer.parseInt(number.substring(3)));
            flightNumber.setCreatedDate(currentDate);
        }
        flightNumber.setLastSeenDate(currentDate);
        flightNumberService.save(flightNumber);
    }

    public String getFlightDetails(String flightNumber) {
        JSONObject jsonObject = flightRadarApiClient.getFlightDetails(flightNumber);

        Map results = (Map) ((Map) jsonObject.get("result")).get("response");
        for (int i = 0; i < (Integer) ((Map) results.get("item")).get("current"); i++) {
            mergeFlightDetails((ArrayList) results.get("data"));
        }
        return "Flight Details of " + flightNumber + " have been precessed successfully";
    }

    private void mergeFlightDetails(ArrayList flightDetail) {
        //flightDetailService.findByFlight_numberAndReal_dep_time("","");
        System.out.println(flightDetail.toString());

    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

    public JSONObject getFlightDetails(String query, String fetchBy, String limit, String page) {
        return flightRadarApiClient.getFlightDetails(query);
    }

}
