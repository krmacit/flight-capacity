package com.cargis.flightcapacity.client.flightradar;

import com.cargis.flightcapacity.configuration.FlightRadarClientConfiguration;
import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "flight-radar", url = "${endpoints.flight-radar}", configuration = FlightRadarClientConfiguration.class)
public interface FlightRadarClient {

    @RequestMapping(method = RequestMethod.GET, value = "/zones/fcgi/feed.js")
    JSONObject getFlights();

}
