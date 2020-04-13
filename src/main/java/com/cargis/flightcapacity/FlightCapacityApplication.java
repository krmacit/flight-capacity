package com.cargis.flightcapacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@SpringBootApplication
@EnableFeignClients
public class FlightCapacityApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightCapacityApplication.class, args);
    }

}