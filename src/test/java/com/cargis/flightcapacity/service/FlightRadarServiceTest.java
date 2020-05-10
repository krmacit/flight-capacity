package com.cargis.flightcapacity.service;

import com.cargis.flightcapacity.client.flightradar.FlightRadarApiClient;
import com.cargis.flightcapacity.client.flightradar.FlightRadarClient;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static com.cargis.flightcapacity.service.FlightRadarService.maximumLatitude;
import static com.cargis.flightcapacity.service.FlightRadarService.minimumLatitude;
import static com.cargis.flightcapacity.service.FlightRadarService.longitudeStep;

@RunWith(MockitoJUnitRunner.class)
public class FlightRadarServiceTest {

    @InjectMocks
    private FlightRadarService flightRadarService;

    @Mock
    private FlightRadarClient flightRadarClient;

    @Mock
    private FlightRadarApiClient flightRadarApiClient;

    @Mock
    private FlightNumberService flightNumberService;

    @Mock
    private FlightDetailService flightDetailService;

    @Before
    public void init() {
        ReflectionTestUtils.setField(flightRadarService, "nonValidKeys", Arrays.asList("full_count", "version"));
    }

    @Test
    public void shouldGetFlights() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(flightRadarService.getNonValidKeys().get(0), "");

        when(flightRadarClient.getFlights(anyString())).thenReturn(jsonObject);

        flightRadarService.getFlights();

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(flightRadarClient, atLeastOnce()).getFlights(argumentCaptor.capture());

        List<String> arguments = argumentCaptor.getAllValues();
        for (String argument : arguments) {
            String[] coordinates = argument.split(",");
            assertEquals(maximumLatitude.toString(), coordinates[0]);
            assertEquals(minimumLatitude.toString(), coordinates[1]);

            BigDecimal longitudeDifference = (new BigDecimal(coordinates[3])).subtract(new BigDecimal(coordinates[2]));

            assertEquals(longitudeStep, longitudeDifference);
        }
    }
}