package com.cargis.flightcapacity.client.flightradar;

import com.cargis.flightcapacity.configuration.FlightRadarClientConfiguration;
import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "flight-radar-api", url = "${endpoints.flight-radar-api}", configuration = FlightRadarClientConfiguration.class)
public interface FlightRadarApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/common/v1/flight/list.json?query={query}&fetchBy=flight&limit=100&page=1")
    JSONObject getFlightDetails(@PathVariable String query);

}
