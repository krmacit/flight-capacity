package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarApiClient;
import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.model.FlightDetail;
import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
        ArrayList<HashMap> allFlightDataList = (ArrayList) results.get("data");
        for (int i = 0; i < itemCount ; i++) {
            mergeFlightDetails(allFlightDataList.get(i));
        }
        log.info("Flight Details with: {} have been processed successfully", flightNumber);
    }

    private void mergeFlightDetails(HashMap flightDetail) {
        HashMap flightIdentification = (HashMap) flightDetail.get("identification");
        HashMap flightStatus = (HashMap) flightDetail.get("status");
        HashMap flightAircraft = (HashMap) flightDetail.get("aircraft");
        HashMap flightOwner = (HashMap) flightDetail.get("owner");
        HashMap flightOriginDetails = (HashMap)((HashMap) flightDetail.get("airport")).get("origin");
        HashMap flightDestinationDetails = (HashMap)((HashMap) flightDetail.get("airport")).get("destination");
        HashMap flightActualTimeDetails = (HashMap)((HashMap) flightDetail.get("time")).get("real");
        HashMap flightScheduledTimeDetails = (HashMap)((HashMap) flightDetail.get("time")).get("scheduled");
        Date departureTime = DateUtils.epochToDate((Integer)flightActualTimeDetails.get("departure"));
        String flightNumber = (String)((HashMap) flightIdentification.get("number")).get("default");

        Optional<FlightDetail> optional = flightDetailService.findByFlightNumberAndActualDepartureTime(
                flightNumber, departureTime);

        FlightDetail currentFlightDetail;

        if (optional.isPresent()){
            currentFlightDetail = optional.get();
        } else {
            currentFlightDetail = new FlightDetail();
            String aircraftModel = (String)((HashMap) flightAircraft.get("model")).get("code");
            String originAirport = (String) ((HashMap)flightOriginDetails.get("code")).get("iata");
            String originCity = (String) ((HashMap)((HashMap)flightOriginDetails.get("position")).get("region")).get("city");
            String originCountry = (String) ((HashMap)((HashMap)flightOriginDetails.get("position")).get("country")).get("name");

            String destinationAirport = (String) ((HashMap)flightDestinationDetails.get("code")).get("iata");
            String destinationCity = (String) ((HashMap)((HashMap)flightDestinationDetails.get("position")).get("region")).get("city");
            String destinationCountry = (String) ((HashMap)((HashMap)flightDestinationDetails.get("position")).get("country")).get("name");

            currentFlightDetail.setFlightNumber(flightNumber);
            currentFlightDetail.setAircraftModel(aircraftModel);

            currentFlightDetail.setOriginAirport(originAirport);
            currentFlightDetail.setOriginCity(originCity);
            currentFlightDetail.setOriginCountry(originCountry);

            currentFlightDetail.setDestinationAirport(destinationAirport);
            currentFlightDetail.setDestinationCity(destinationCity);
            currentFlightDetail.setDestinationCountry(destinationCountry);

            currentFlightDetail.setAirlineName(destinationCountry);

            currentFlightDetail.setScheduledDepartureTime(DateUtils.epochToDate((Integer) flightScheduledTimeDetails.get("departure")));
            currentFlightDetail.setScheduledArrivalTime(DateUtils.epochToDate((Integer) flightScheduledTimeDetails.get("arrival")));
            currentFlightDetail.setActualDepartureTime(DateUtils.epochToDate((Integer) flightActualTimeDetails.get("departure")));
            currentFlightDetail.setActualArrivalTime(DateUtils.epochToDate((Integer) flightActualTimeDetails.get("arrival")));
        }
        flightDetailService.save(currentFlightDetail);
    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

    public JSONObject getFlightDetails(String query, String fetchBy, String limit, String page) {
        return flightRadarApiClient.getFlightDetails(query);
    }

}
