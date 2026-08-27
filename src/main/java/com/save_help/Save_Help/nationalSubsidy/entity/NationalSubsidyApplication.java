package com.save_help.Save_Help.nationalSubsidy.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "national_subsidy_application",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_subsidy",
                columnNames = {"user_id", "subsidy_id"}
        ),
        indexes = {
                @Index(name = "idx_app_user", columnList = "user_id"),
                @Index(name = "idx_app_subsidy", columnList = "subsidy_id"),
                @Index(name = "idx_app_status", columnList = "status")
        }
)
public class NationalSubsidyApplication {

    public enum Status { PENDING, APPLIED, APPROVED, PAID }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subsidy_id")
    private NationalSubsidy subsidy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(length = 1000)
    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private AppliedBy appliedBy;

    @Column(name = "event_id", length = 100)
    private String eventId;

    private LocalDateTime appliedAt; // 신청 완료 시간

    @Version
    private Long version;

    public enum AppliedBy {
        AUTO, MANUAL, ADMIN, SYSTEM
    }

    @PrePersist
    void prePersist() {
        if (status == null) status = Status.PENDING;
        if (appliedBy == null) appliedBy = AppliedBy.SYSTEM;
    }

    public void markApplied(String reason) {
        this.status = Status.APPLIED;
        this.reason = reason;
        this.appliedAt = LocalDateTime.now();
    }

    public void markPending(String reason) {
        this.status = Status.PENDING;
        this.reason = reason;
    }

    public void markAppliedAuto(String reason, String eventId) {
        this.appliedBy = AppliedBy.AUTO;
        this.eventId = eventId;
        markApplied(reason);
    }

    public void markAppliedManual(String reason, String eventId) {
        this.appliedBy = AppliedBy.MANUAL;
        this.eventId = eventId;
        markApplied(reason);
    }

    public boolean isApplied() {
        return this.status == Status.APPLIED;
    }


    public boolean isSameEvent(String eventId) {
        return eventId != null && eventId.equals(this.eventId);
    }

    public void approve(String reason) {

        if (!canApprove()) {
            throw new IllegalStateException(
                    "IllegalStateException : " + status
            );
        }

        this.status = Status.APPROVED;
        this.reason = reason;
    }

    public boolean isAutoApplied() {
        return this.appliedBy == AppliedBy.AUTO;
    }

    public boolean isManualApplied() {
        return this.appliedBy == AppliedBy.MANUAL;
    }

    public boolean canApprove() {
        return this.status == Status.APPLIED
                || this.status == Status.PENDING;
    }

    public boolean isPaid() {
        return this.status == Status.PAID;
    }

}
