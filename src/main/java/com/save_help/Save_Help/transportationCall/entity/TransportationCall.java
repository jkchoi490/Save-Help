package com.save_help.Save_Help.transportationCall.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TransportationCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 호출 요청 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requester;

    // 배정된 차량 (택시, 이동 지원 차량 등)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    // 출발 위치
    private Double pickupLatitude;
    private Double pickupLongitude;

    // 도착 위치
    private Double dropoffLatitude;
    private Double dropoffLongitude;

    // 호출 시각
    private LocalDateTime requestedAt;

    // 차량 출발 시각
    private LocalDateTime dispatchedAt;

    // 목적지 도착 시각
    private LocalDateTime arrivedAt;

    // 호출 상태
    @Enumerated(EnumType.STRING)
    private TransportationCallStatus status;

    // 호출 유형 (택시, 셔틀, 자가용 등)
    @Enumerated(EnumType.STRING)
    private TransportationType type;

    // 기본 생성자
    protected TransportationCall() {}

    // 생성자
    public TransportationCall(User requester, Double pickupLat, Double pickupLon,
                              Double dropLat, Double dropLon, TransportationType type) {
        this.requester = requester;
        this.pickupLatitude = pickupLat;
        this.pickupLongitude = pickupLon;
        this.dropoffLatitude = dropLat;
        this.dropoffLongitude = dropLon;
        this.type = type;
        this.status = TransportationCallStatus.REQUESTED;
        this.requestedAt = LocalDateTime.now();
    }

}
