package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarApiClient;
import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.model.FlightNumber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.cargis.flightcapacity.util.DateUtils.getCurrentDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightRadarService {

    public static final BigDecimal minimumLatitude = BigDecimal.valueOf(-89.95D).setScale(2, RoundingMode.HALF_EVEN);
    public static final BigDecimal maximumLatitude = BigDecimal.valueOf(89.95D).setScale(2, RoundingMode.HALF_EVEN);
    public static final BigDecimal minimumLongitude = BigDecimal.valueOf(-180).setScale(2, RoundingMode.HALF_EVEN);
    public static final BigDecimal maximumLongitude = BigDecimal.valueOf(180).setScale(2, RoundingMode.HALF_EVEN);
    public static final BigDecimal longitudeStep = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_EVEN);

    @Getter
    @Value("#{'flight-radar.api.get-flights.non-valid-keys'.split(';')}")
    public List<String> nonValidKeys;

    private final FlightRadarClient flightRadarClient;
    private final FlightRadarApiClient flightRadarApiClient;
    private final FlightNumberService flightNumberService;
    private final FlightDetailService flightDetailService;

    public void getFlights() {
        for (BigDecimal value = minimumLongitude; value.compareTo(maximumLongitude) < 0; value = value.add(longitudeStep)) {
            String bounds = String.join(",", maximumLatitude.toString(), minimumLatitude.toString(),
                    value.toString(), value.add(longitudeStep).toString());
            JSONObject jsonObject = flightRadarClient.getFlights(bounds);
            Date currentDate = getCurrentDate();
            for (Object key : jsonObject.keySet()) {
                if (BooleanUtils.isFalse(nonValidKeys.contains(key))) {
                    String[] values = jsonObject.get(key).toString().split(",");
                    String number = values[13];
                    String flightCodeString = number.substring(3);

                    if (number.length() < 3 || BooleanUtils.isFalse(NumberUtils.isCreatable(flightCodeString))) {
                        continue;
                    }

                    String carrierCode = number.substring(0, 3);
                    Integer flightCode = NumberUtils.createInteger(flightCodeString);
                    mergeFlightNumber(number, carrierCode, flightCode, currentDate);
                }
            }
        }
    }

    private void mergeFlightNumber(String number, String carrierCode, Integer flightCode, Date currentDate) {
        Optional<FlightNumber> optional = flightNumberService.findByNumber(number);
        FlightNumber flightNumber;
        if (optional.isPresent()) {
            flightNumber = optional.get();
        } else {
            flightNumber = new FlightNumber();
            flightNumber.setNumber(number);
            flightNumber.setCarrierCode(carrierCode);
            flightNumber.setFlightCode(flightCode);
            flightNumber.setCreatedDate(currentDate);
        }
        flightNumber.setLastSeenDate(currentDate);
        flightNumberService.save(flightNumber);
    }

    public void getFlightDetails(String flightNumber) {
        JSONObject jsonObject = flightRadarApiClient.getFlightDetails(flightNumber);
        Map results = (Map) ((Map) jsonObject.get("result")).get("response");
        int itemCount = (Integer) ((Map) results.get("item")).get("current");
        for (int i = 0; i < itemCount ; i++) {
            mergeFlightDetails((ArrayList) results.get("data"));
        }
        log.info("Flight Details with: {} have been processed successfully", flightNumber);
    }

    private void mergeFlightDetails(ArrayList flightDetail) {
        log.info(flightDetail.toString());
    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

    public JSONObject getFlightDetails(String query, String fetchBy, String limit, String page) {
        return flightRadarApiClient.getFlightDetails(query);
    }

}
