package com.save_help.Save_Help.nationalSubsidy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private NationalSubsidyType type;

    @Column(length = 1000)
    private String center; // 보조금 지원처

    // 최대 지원 금액
    @Column(nullable = false)
    private Integer maxAmount;

    // 대상
    private String targetGroup;

    // 신청 시작일 / 종료일
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    // 현재 활성화 여부
    private boolean active = true;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    @Column(nullable = false)
    private long applicationCount; //신청 수

    private Integer minAge;
    private Integer maxAge;
    private String incomeLevel;
    private Boolean disabilityRequired;
    private Boolean emergencyOnly;

    private String subsidyType; // 보조금 종류
    private String subsidyField; // 보조금 분야

    @Version
    private Long version;

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

    private long approvedCount;   // 승인 건수

    @Column(name = "event_id", unique = true, nullable = false, length = 100)
    private String eventId;

    public boolean isRunnable(LocalDate today) {
        if (!active) return false;
        if (!isOpen) return false;
        if (startDate != null && startDate.isAfter(today)) return false;
        if (endDate != null && endDate.isBefore(today)) return false;
        return true;
    }

    public void open() {
        this.isOpen = true;
        this.openedAt = LocalDateTime.now();
        this.closedAt = null;
    }
    public void close() {
        this.isOpen = false;
        this.closedAt = LocalDateTime.now();
    }
    public void activate() { this.active = true; }
    public void deactivate() { this.active = false; }


    @PrePersist
    @PreUpdate
    void validate() {
        if (name == null || name.isBlank()) throw new IllegalStateException("name is required");
        if (maxAmount == null || maxAmount < 0) throw new IllegalStateException("maxAmount must be >= 0");

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalStateException("startDate must be <= endDate");
        }
    }

    public void increaseApplicationCount(long delta) {
        if (delta <= 0) return;
        this.applicationCount += delta;
    }

    public void reopen() {
        this.isOpen = true;
        this.openedAt = LocalDateTime.now();
    }

    public void increaseApprovedCount(long delta) {
        if (delta > 0) this.approvedCount += delta;
    }

    public boolean isEmergencyEligible(boolean emergency) {
        if (Boolean.TRUE.equals(emergencyOnly) && !emergency) return false;
        return true;
    }

    public double getExecutionRate() {
        if (currentBudgetAmount <= 0) {
            return 0.0;
        }

        return (double) executedAmount / currentBudgetAmount * 100.0;
    }
}
