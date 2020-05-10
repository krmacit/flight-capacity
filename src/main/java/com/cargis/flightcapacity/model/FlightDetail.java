package com.cargis.flightcapacity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="FLIGHT_DETAIL")
@SequenceGenerator(name = "SEQ_FLIGHT_DETAIL", sequenceName = "SEQ_FLIGHT_DETAIL")
public class FlightDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID")
    private Long id;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "AIRCRAFT_MODEL")
    private String aircraftModel;

    @Column(name = "ORIGIN_AIRPORT")
    private String originAirport;

    @Column(name = "ORIGIN_CITY")
    private String originCity;

    @Column(name = "ORIGIN_COUNTRY")
    private String originCountry;

    @Column(name = "DESTINATION_AIRPORT")
    private String destinationAirport;

    @Column(name = "DESTINATION_CITY")
    private String destinationCity;

    @Column(name = "DESTINATION_COUNTRY")
    private String destinationCountry;

    @Column(name = "AIRLINE_NAME")
    private String airlineName;

    @Column(name = "SCHEDULED_DEPARTURE_TIME")
    private Date scheduledDepartureTime;

    @Column(name = "SCHEDULED_ARRIVAL_TIME")
    private Date scheduledArrivalTime;

    @Column(name = "ACTUAL_DEPARTURE_TIME")
    private Date actualDepartureTime;

    @Column(name = "ACTUAL_ARRIVAL_TIME")
    private Date actualArrivalTime;
}