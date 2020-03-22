package com.cargis.flightcapacity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = DevelopmentController.ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DevelopmentController {

    public static final String ENDPOINT = "dev";

    @RequestMapping
    public ResponseEntity<String> test() {
        log.info("test");
        return ResponseEntity.ok("test");
    }
}