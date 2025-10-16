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
public class HelperEmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연락 주체: USER → HELPER or HELPER → USER
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    // 메시지 내용
    @Column(nullable = false, length = 500)
    private String message;

    // 연락 시간
    private LocalDateTime timestamp = LocalDateTime.now();

    // 읽음 여부
    private boolean read = false;

    // 보낸 사람과 받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id")
    private Helper helper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
