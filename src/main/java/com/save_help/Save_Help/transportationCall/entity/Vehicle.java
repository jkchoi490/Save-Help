package com.save_help.Save_Help.transportationCall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleNumber; // 차량 번호

    private String driverName;
    private String phoneNumber;

    // 현재 위치
    private Double latitude;
    private Double longitude;

    private boolean available = true;

    @OneToMany(mappedBy = "vehicle")
    private List<TransportationCall> calls = new ArrayList<>();
}