package com.save_help.Save_Help.messaging.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userPhone;     // 사용자 번호
    private String helperPhone;   // 상담사 번호
    private boolean active;       // 활성 세션 여부
    private LocalDateTime startedAt;
    private LocalDateTime lastMessageAt;
}