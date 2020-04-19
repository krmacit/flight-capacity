package com.cargis.flightcapacity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FLIGHT_DETAILS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FlightDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID")
    private Long id;

    @Column(name="FLIGHT_NUMBER")
    private String flight_number;

    @Column(name="AIRCRAFT_MODEL")
    private String aircraft_model;

    @Column(name="ORIGIN_AIRPORT")
    private String origin_airport;

    @Column(name="ORIGIN_CITY")
    private String origin_city;

    @Column(name="ORIGIN_COUNTRY")
    private String origin_country;

    @Column(name="DESTINATION_AIRPORT")
    private String destination_airport;

    @Column(name="DESTINATION_CITY")
    private String destination_city;

    @Column(name="DESTINATION_COUNTRY")
    private String destination_country;

    @Column(name="AIRLINE_NAME")
    private String airline_name;

    @Column(name="SCH_DEP_TIME")
    private String sch_dep_time;

    @Column(name="SCH_ARR_TIME")
    private String sch_arr_time;

    @Column(name="REAL_DEP_TIME")
    private String real_dep_time;

    @Column(name="REAL_ARR_TIME")
    private String real_arr_time;
}