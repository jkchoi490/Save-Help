package com.save_help.Save_Help.chat.entity;

import com.save_help.Save_Help.call.entity.Call;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 메시지 발신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    // 메시지 수신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    // 메시지 내용
    @Column(nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    // 메시지 전송 시간
    private LocalDateTime sentAt;

    // 읽음 여부
    @Column(name = "is_read", nullable = false)
    private boolean read;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id")
    private Call call;
}
