package com.save_help.Save_Help.helper.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 헬퍼 (작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id")
    private Helper helper;

    @Column(nullable = false, length = 200)
    private String title; // 피드백 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 피드백 본문

    @Enumerated(EnumType.STRING)
    private FeedbackStatus status = FeedbackStatus.PENDING; // 처리 상태

    @Column(length = 500)
    private String adminResponse; // 관리자 답변 내용

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public enum FeedbackStatus {
        PENDING,    // 대기 중
        IN_PROGRESS,
        RESOLVED,
        REJECTED
    }

    public void respond(String response, FeedbackStatus newStatus) {
        this.adminResponse = response;
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
