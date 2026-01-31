package com.save_help.Save_Help.nationalSubsidy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "national_subsidy")
public class NationalSubsidy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 보조금 이름 (예: "응급 의료비 지원", "심리상담 지원금")
    @Column(nullable = false, length = 200)
    private String name;

    // 보조금 설명
    @Column(length = 1000)
    private String description;

    // 보조금 종류 (의료, 상담, 재난, 생활비 등)
    @Enumerated(EnumType.STRING)
    private SubsidyType type;

    @Column(length = 1000)
    private String center; // 보조금 지원처

    // 최대 지원 금액
    private Integer maxAmount;

    // 대상
    private String targetGroup;

    // 신청 시작일 / 종료일
    private LocalDate startDate;
    private LocalDate endDate;

    // 현재 활성화 여부
    private boolean active = true;

    private Integer minAge;
    private Integer maxAge;
    private String incomeLevel;
    private Boolean disabilityRequired;
    private Boolean emergencyOnly;

    //공공데이터
    private String baseDate;          //기준일자
    private String policyAreaCode;    // 분야 코드
    private String policyAreaName;    // 분야명
    private String categoryCode;      // 부문 코드
    private String categoryName;      // 부문명
    private int fiscalYear;           // 사업연도
    private long currentBudgetAmount; // 현행 예산액
    private long originalBudgetAmount; // 기정 예산액
    private long executedAmount;      // 집행 금액

}
