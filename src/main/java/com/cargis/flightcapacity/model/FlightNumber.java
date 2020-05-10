package com.cargis.flightcapacity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="FLIGHT_NUMBER")
@SequenceGenerator(name = "SEQ_FLIGHT_NUMBER", sequenceName = "SEQ_FLIGHT_NUMBER")
public class FlightNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "CARRIER_CODE")
    private String carrierCode;

    @Column(name = "FLIGHT_CODE")
    private Integer flightCode;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "LAST_SEEN_DATE")
    private Date lastSeenDate;

    @Column(name = "LAST_PROCESS_DATE")
    private Date lastProcessDate;

}
