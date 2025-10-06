package com.save_help.Save_Help.hospital.entity;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.helper.entity.Helper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원 이름
    @Column(nullable = false, length = 200)
    private String name;

    // 병원 유형 (종합, 의원, 치과, 한의원 등)
    @Enumerated(EnumType.STRING)
    private HospitalType type;

    // 연락처
    @Column(length = 20)
    private String phoneNumber;

    // 위치 정보
    private Double latitude;
    private Double longitude;

    // 운영 여부
    private boolean active = true;

    // 소속 MedicalStaff / Helper 리스트
    @OneToMany(mappedBy = "hospital")
    private List<Helper> medicalStaff = new ArrayList<>();

    // 긴급 상황 처리 Emergency 리스트
    @OneToMany(mappedBy = "hospital")
    private List<Emergency> emergencies = new ArrayList<>();
}
