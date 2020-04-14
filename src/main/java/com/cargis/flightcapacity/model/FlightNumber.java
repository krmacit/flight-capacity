package com.cargis.flightcapacity.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="FLIGHT_NUMBER")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class FlightNumber {

    public FlightNumber(String number, String carrierCode, int flightCode){
        this.number = number;
        this.carrierCode = carrierCode;
        this.flightCode = flightCode;
    }

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

}
