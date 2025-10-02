package com.save_help.Save_Help.communityCenter;

import com.save_help.Save_Help.emergency.Emergency;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CommunityCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 센터 이름 (예: "서울 강남소방서", "OO병원")
    @Column(nullable = false, length = 100)
    private String name;

    // 센터 타입
    @Enumerated(EnumType.STRING)
    private CenterType type;

    // 위치 정보 (위도, 경도)
    private Double latitude;
    private Double longitude;

    // 연락 가능한 전화번호
    @Column(length = 20)
    private String phoneNumber;

    // 운영 여부
    private boolean active = true;

    // 이 센터가 처리한 Emergency 요청들
    @OneToMany(mappedBy = "assignedCenter")
    private List<Emergency> emergencies = new ArrayList<>();
}
