package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import com.cargis.flightcapacity.controller.FlightNumberController;
import com.cargis.flightcapacity.model.FlightNumber;
import com.cargis.flightcapacity.repository.FlightNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FlightRadarService {

    @Autowired
    private FlightRadarClient flightRadarClient;

    @Autowired
    private FlightNumberService flightNumberService;

    public ArrayList<String> getFlights() throws InterruptedException {
        String bound, id;
        ArrayList<String> flightIds = new ArrayList<String>();
        for (int i = 0; i < 18; i++) {
            bound = "89.93,-89.95," + (-180 + i * 20) + ".00," + (-180 + (i + 1) * 20) + ".00";
            Iterator<String> itr = flightRadarClient.getFlights(bound).keySet().iterator();
            while (itr.hasNext()) {
                id = itr.next();
                if (!id.equals("full_count") && !id.equals("version")) flightIds.add(id);
            }
            FlightNumber fN = new FlightNumber((long) 12,"TKden", "11", 11);
            log.info(fN.toString());
            flightNumberService.create(fN);
        }
        //getAllFlightsDetails(flightIds);
        return flightIds;
    }

    public void getAllFlightsDetails(ArrayList<String> flightIds) throws InterruptedException {
        int count = 0;
        for (String id : flightIds) {
            getFlightDetail(id, "1.5");
            count++;
            if (count == 20) {
                //TimeUnit.SECONDS.sleep(5);
                count = 0;
            }
        }
    }

    public JSONObject getFlightDetail(String flightId, String version) {
        return flightRadarClient.getFlightDetail(flightId, version);
    }

}