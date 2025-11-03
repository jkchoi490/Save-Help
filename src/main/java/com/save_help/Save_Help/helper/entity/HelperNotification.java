package com.save_help.Save_Help.helper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class HelperNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 수신자 (헬퍼)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id")
    private Helper helper;

    // 알림 유형: SMS, PUSH, SYSTEM 등
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    // 알림 메시지
    @Column(columnDefinition = "TEXT")
    private String message;

    // 발송 시각
    private LocalDateTime sentAt;

    // 발송 성공 여부
    private boolean success;

    // 실패 사유 (optional)
    private String errorReason;

    @PrePersist
    public void onCreate() {
        this.sentAt = LocalDateTime.now();
    }

    public enum NotificationType {
        SMS, PUSH, SYSTEM
    }
}
