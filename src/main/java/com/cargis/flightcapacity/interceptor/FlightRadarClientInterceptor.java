package com.cargis.flightcapacity.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class FlightRadarClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info(requestTemplate.url());
        requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (HTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
    }
}
