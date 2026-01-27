package com.save_help.Save_Help.counseling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(
        indexes = {
                @Index(name="idx_notif_recipient_created", columnList = "recipientType,recipientId,createdAt"),
                @Index(name="idx_notif_unique_key", columnList = "uniqueKey", unique = true)
        }
)
public class CounselingNotification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecipientType recipientType; // USER, HELPER

    @Column(nullable = false)
    private Long recipientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private NotificationType type; // COUNSELING_REMINDER, COUNSELING_STATUS

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(length = 500)
    private String deeplink;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 중복 방지용(리마인더는 스케줄러가 반복 실행되므로 필수)
    @Column(nullable = false, length = 120, unique = true)
    private String uniqueKey;
}
