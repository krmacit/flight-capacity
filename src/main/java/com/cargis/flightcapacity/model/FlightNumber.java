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
import java.time.OffsetDateTime;

@Entity
@Table(name="FLIGHT_NUMBER")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FlightNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="ID")
    private Long id;

    @Column(name="NUMBER")
    private String number;

    @Column(name="CARRIER_CODE")
    private String carrierCode;

    @Column(name="FLIGHT_CODE")
    private Integer flightCode;

    @Column(name="CREATED_DATE")
    private OffsetDateTime createdDate;

    @Column(name="LAST_SEEN_DATE")
    private OffsetDateTime lastSeenDate;

    @Column(name="UPDATED_DATE")
    private OffsetDateTime updatedDate;

}
