package com.cargis.flightcapacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlightCapacityApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightCapacityApplication.class, args);
    }

}