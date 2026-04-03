package com.save_help.Save_Help.dailyNecessities.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_necessity_user_support_period",
                        columnNames = {"user_id", "support_id", "period_key"}
                )
        }
)
public class DailyNecessityApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long supportId;

    private String status;          // PENDING, APPROVED, REJECTED, ALLOCATED ...
    private String applyType;       // AUTO, MANUAL
    private String periodKey;       // 월 단위 키
    private String reason;          // 자동 신청 사유
    private LocalDateTime appliedAt;

    private String description; // 생필품 신청 내용

    @Column(nullable = false)
    private Integer quantity;

    private Long centerId;

    public String ApplyType() {
        return this.applyType;
    }

}