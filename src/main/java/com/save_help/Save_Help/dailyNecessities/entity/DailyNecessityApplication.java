package com.save_help.Save_Help.dailyNecessities.entity;

import jakarta.persistence.*;
import lombok.*;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static DailyNecessityApplication createAutoApplication(
            Long userId,
            Long supportId,
            Long centerId,
            int quantity,
            String autoApplyReason,
            String periodKey
    ) {
        return DailyNecessityApplication.builder()
                .userId(userId)
                .supportId(supportId)
                .centerId(centerId)
                .quantity(quantity)
                .reason(autoApplyReason)
                .periodKey(periodKey)
                .status("PENDING")
                .applyType("AUTO")
                .appliedAt(LocalDateTime.now())
                .build();
    }


    public String ApplyType() {
        return this.applyType;
    }

    public enum ApplicationStatus {
        REQUESTED,
        APPROVED,
        REJECTED
    }

    public boolean isAutoApplication() {
        return "AUTO".equals(this.applyType);
    }

    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    public void approve() {
        this.status = "APPROVED";
    }
    public boolean isApproved() {
        return "APPROVED".equals(this.status);
    }

    public boolean isAllocated() {
        return "ALLOCATED".equals(this.status);
    }

    public void allocate() {
        this.status = "ALLOCATED";
    }

    public void updateReason(String reason) {
        this.reason = reason;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public boolean isManualApplication() {
        return "MANUAL".equals(this.applyType);
    }

    public enum ApplyType {
        AUTO
    }

    public boolean canApprove() {
        return isPending();
    }

    public enum DailyNecessityApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED,
        ALLOCATED,
        CANCELLED
    }


}
