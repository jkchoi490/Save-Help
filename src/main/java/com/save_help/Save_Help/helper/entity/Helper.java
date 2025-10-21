package com.save_help.Save_Help.helper.entity;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Helper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름 (상담사 이름, 의사 이름 등)
    @Column(nullable = false, length = 100)
    private String name;

    // 역할 (MEDICAL, COUNSELOR, POLICE 등)
    @Enumerated(EnumType.STRING)
    private HelperRole role;

    // 소속 (어느 센터나 병원에 소속되어 있는지)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter communityCenter;

    // 연락 가능한 전화번호
    @Column(length = 20)
    private String phoneNumber;

    // 근무 가능 여부
    private boolean available = true;

    // 담당한 긴급 상황들
    @OneToMany(mappedBy = "assignedHelper")
    private List<Emergency> emergencies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "helper", cascade = CascadeType.ALL)
    private List<HelperEmergencyContact> helperEmergencyContacts = new ArrayList<>();

    //@Enumerated(EnumType.STRING)
    //private HelperActivityStatus activityStatus = HelperActivityStatus.OFF_DUTY;

    // 활동 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelperActivityStatus activityStatus = HelperActivityStatus.AVAILABLE;

    // 신뢰도 점수 (평균 평점 기반)
    @Column(nullable = false)
    private Double trustScore = 5.0;

    // 최근 평가 갱신일 (선택사항)
    private java.time.LocalDateTime lastReviewUpdate;

    // 신뢰도 업데이트 메서드
    public void updateTrustScore(Double newScore) {
        this.trustScore = newScore;
        this.lastReviewUpdate = java.time.LocalDateTime.now();

        // 평균 평점이 2.5 미만이면 상태 변경
        if (newScore < 2.5) {
            this.activityStatus = HelperActivityStatus.REVIEW_REQUIRED;
        } else if (this.activityStatus == HelperActivityStatus.REVIEW_REQUIRED && newScore >= 3.0) {
            // 신뢰도 회복 시 복귀
            this.activityStatus = HelperActivityStatus.AVAILABLE;
        }
    }
}
