package com.save_help.Save_Help.Help.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "help",
        indexes = {
                @Index(name = "idx_help_requester_created", columnList = "requester_id, created_at"),
                @Index(name = "idx_help_status_created", columnList = "status, created_at"),
                @Index(name = "idx_help_type_created", columnList = "type, created_at")
        }
)
public class Help {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 도움을 요청한 사용자 (요청자)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    /**
     * 도움 종류 (긴급/상담/이송/생필품/기타)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HelpType type;

    /**
     * 케이스 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HelpStatus status;

    /**
     * 사용자가 입력한 요약/설명 (공통 필드)
     */
    @Column(nullable = false, length = 500)
    private String description;


    /**
     * 처리 완료/종료 시각
     */
    private LocalDateTime completedAt;

    /**
     * 생성/수정 시각
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 730)
    private String feedback;

    @PrePersist
    private void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) this.status = HelpStatus.REQUESTED;
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ---------------------------
    // 상태 전환 도메인 메서드
    // ---------------------------

    public void markMatching() {
        this.status = HelpStatus.MATCHING;
    }

    public void markInProgress() {
        this.status = HelpStatus.IN_PROGRESS;
    }

    public void markCompleted() {
        this.status = HelpStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void markCancelled() {
        this.status = HelpStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
    }
}
