package com.cargis.flightcapacity.configuration;

import com.cargis.flightcapacity.interceptor.FlightRadarClientInterceptor;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightRadarClientConfiguration {

    @Bean
    public FlightRadarClientInterceptor requestInterceptor() {
        return FlightRadarClientInterceptor.builder().build();
    }

}