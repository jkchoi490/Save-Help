package com.save_help.Save_Help.AmbulanceCall.entity;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class AmbulanceCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 호출을 요청한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requester;

    // 배정된 응급차 (Ambulance 엔티티가 있으면 연동 가능)
   // @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "ambulance_id")
   // private Ambulance ambulance;

    // 호출과 연결된 긴급 상황
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id")
    private Emergency emergency;

    // 호출 시각
    private LocalDateTime requestedAt;

    // 출발 시각
    private LocalDateTime dispatchedAt;

    // 도착 시각
    private LocalDateTime arrivedAt;

    // 상태
    @Enumerated(EnumType.STRING)
    private AmbulanceCallStatus status;
}
