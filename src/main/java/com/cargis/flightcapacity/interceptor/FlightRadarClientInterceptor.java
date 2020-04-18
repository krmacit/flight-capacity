package com.cargis.flightcapacity.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Builder;

@Builder
public class FlightRadarClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (HTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
    }
}
