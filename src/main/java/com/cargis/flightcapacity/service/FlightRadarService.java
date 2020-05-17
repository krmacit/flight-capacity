package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarApiClient;
import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.model.FlightDetail;
import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    @Value("${flight-radar.api.get-flights.non-valid-keys}")
    public List<String> nonValidKeys;

    private final FlightRadarClient flightRadarClient;
    private final FlightRadarApiClient flightRadarApiClient;
    private final FlightNumberService flightNumberService;
    private final FlightDetailService flightDetailService;

    @SneakyThrows
    public void startApp(){
        int count = 0;
        getFlights();
        while (true){
            new Thread(() -> getFlights()).start();
            if (count %96 == 0){
                new Thread(() -> getAllFlightDetails()).start();
            }
            count++;
            log.info("All flights are acquired {}. times.",count);
            TimeUnit.MINUTES.sleep(15);
        }
    }

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

                    if (number.startsWith(" ")){
                        number = number.substring(1);
                    }

                    if (number.length() < 3 || BooleanUtils.isFalse(NumberUtils.isParsable(number.substring(2)))) {
                        continue;
                    }

                    String carrierCode = number.substring(0, 2);
                    Integer flightCode = Integer.parseInt(number.substring(2));
                    mergeFlightNumber(number, carrierCode, flightCode, currentDate, (String) key);
                }
            }
        }
    }

    private void mergeFlightNumber(String number, String carrierCode, Integer flightCode, Date currentDate, String flightRadarID) {
        Optional<FlightNumber> optional = flightNumberService.findByNumber(number);
        FlightNumber flightNumber;
        if (optional.isPresent()) {
            flightNumber = optional.get();
            if (BooleanUtils.isFalse(flightNumber.getFlightRadarID() == flightRadarID)) {
                flightNumber.setLastSeenDate(currentDate);
                flightNumber.setFlightRadarID(flightRadarID);
            }
        } else {
            flightNumber = new FlightNumber();
            flightNumber.setNumber(number);
            flightNumber.setCarrierCode(carrierCode);
            flightNumber.setFlightCode(flightCode);
            flightNumber.setCreatedDate(currentDate);
            flightNumber.setLastProcessDate(currentDate);
            flightNumber.setLastSeenDate(currentDate);
            flightNumber.setFlightRadarID(flightRadarID);
        }
        flightNumberService.save(flightNumber);
    }

    @SneakyThrows
    public void getAllFlightDetails() {
        Date currentDate;
        List<FlightNumber> allFlightNumber = flightNumberService.findAll();
        Integer numberOfFlight = allFlightNumber.size();
        Integer currentFlightNumber = 0;
        Integer cumFlightNumber = 0;
        for (FlightNumber currentFlight : allFlightNumber) {
            if (ObjectUtils.isEmpty(currentFlight.getUpdatedDate()) || currentFlight.getUpdatedDate().after(currentFlight.getLastSeenDate())) {
                getFlightDetails(currentFlight.getNumber());
                currentDate = getCurrentDate();
                currentFlight.setUpdatedDate(currentDate);
                currentFlight.setLastProcessDate(currentDate);
                flightNumberService.save(currentFlight);
                currentFlightNumber++;
                TimeUnit.SECONDS.sleep((long) (4));
                if (currentFlightNumber % 10 == 0) {
                    log.info("{}. pull request for {}. flight detail of {} has been added.", currentFlightNumber, cumFlightNumber, numberOfFlight);
                }
            }
            cumFlightNumber++;
        }
        log.info("All flight details are acquired.");
    }

    @SneakyThrows
    public void getFlightDetails(String flightNumber) {
        JSONObject jsonObject;
        try {
            jsonObject = flightRadarApiClient.getFlightDetails(flightNumber);
        } catch (Exception e) {
            TimeUnit.MINUTES.sleep(1);
            return;
        }
        Map results = (Map) ((Map) jsonObject.get("result")).get("response");
        Boolean nullCheck = true;
        int itemCount = (Integer) ((Map) results.get("item")).get("current");
        ArrayList<HashMap> allFlightDataList = (ArrayList) results.get("data");
        for (int i = 0; i < itemCount ; i++) {
            if (BooleanUtils.isFalse((String) ((HashMap)(allFlightDataList.get(i)).get("identification")).get("id") == null)) {
                mergeFlightDetails(allFlightDataList.get(i));
            }
        }
    }

    private void mergeFlightDetails(HashMap flightDetail) {
        HashMap flightIdentification = (HashMap) flightDetail.get("identification");
        HashMap flightStatus = (HashMap) flightDetail.get("status");
        HashMap flightAircraft = (HashMap) flightDetail.get("aircraft");
        HashMap flightOwner = (HashMap) flightDetail.get("owner");
        HashMap flightAirline = (HashMap) flightDetail.get("airline");
        HashMap flightOriginDetails = (HashMap)((HashMap) flightDetail.get("airport")).get("origin");
        HashMap flightDestinationDetails = (HashMap)((HashMap) flightDetail.get("airport")).get("destination");
        HashMap flightActualTimeDetails = (HashMap)((HashMap) flightDetail.get("time")).get("real");
        HashMap flightScheduledTimeDetails = (HashMap)((HashMap) flightDetail.get("time")).get("scheduled");
        String flightNumber = (String) ((HashMap) flightIdentification.get("number")).get("default");
        String flightRadarDetailID = (String) flightIdentification.get("id");

        Optional<FlightDetail> optional = flightDetailService.findByFlightRadarDetailID(flightRadarDetailID);

        FlightDetail currentFlightDetail;
        if (optional.isPresent()){
            currentFlightDetail = optional.get();
        } else {
            currentFlightDetail = new FlightDetail();

            String aircraftModel = getFromJSON(flightAircraft, Arrays.asList("model", "code"));
            String aircraftModelText = getFromJSON(flightAircraft, Arrays.asList("model", "text"));
            String aircraftRegistration = getFromJSON(flightAircraft, Arrays.asList("registration"));
            String originAirport = getFromJSON(flightOriginDetails, Arrays.asList("code", "iata"));
            String originCity = getFromJSON(flightOriginDetails, Arrays.asList("position", "region", "city"));
            String originCountry = getFromJSON(flightOriginDetails, Arrays.asList("position", "country", "name"));

            String destinationAirport = getFromJSON(flightDestinationDetails, Arrays.asList("code", "iata"));
            String destinationCity = getFromJSON(flightDestinationDetails, Arrays.asList("position", "region", "city"));
            String destinationCountry = getFromJSON(flightDestinationDetails, Arrays.asList("position", "country", "name"));

            String airlineName = getFromJSON(flightAirline, Arrays.asList("name"));
            String airlineShortName = getFromJSON(flightAirline, Arrays.asList("code", "iata"));

            currentFlightDetail.setFlightNumber(flightNumber);
            currentFlightDetail.setAircraftModel(aircraftModel);
            currentFlightDetail.setAircraftModelText(aircraftModelText);
            currentFlightDetail.setAircraftRegistration(aircraftRegistration);

            currentFlightDetail.setFlightRadarDetailID(flightRadarDetailID);

            currentFlightDetail.setOriginAirport(originAirport);
            currentFlightDetail.setOriginCity(originCity);
            currentFlightDetail.setOriginCountry(originCountry);

            currentFlightDetail.setDestinationAirport(destinationAirport);
            currentFlightDetail.setDestinationCity(destinationCity);
            currentFlightDetail.setDestinationCountry(destinationCountry);

            currentFlightDetail.setAirlineName(airlineName);
            currentFlightDetail.setAirlineShortName(airlineShortName);

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

    public String getFromJSON(HashMap json, List<String> keys) {
        for (int i = 0; i < keys.size() - 1; i++) {
            if (BooleanUtils.isFalse(json == null) && json.containsKey(keys.get(i))) {
                json = (HashMap) json.get(keys.get(i));
            }
        }
        if (BooleanUtils.isFalse(json == null) && json.containsKey(keys.get(keys.size() - 1))) {
            return (String) json.get(keys.get(keys.size() - 1));
        } else {
            return null;
        }
    }

}
