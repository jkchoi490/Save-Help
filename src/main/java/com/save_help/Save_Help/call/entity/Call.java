package com.save_help.Save_Help.call.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
public class Call {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User caller;   // 발신자

    @ManyToOne
    private User callee;   // 수신자

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    private CallStatus status; // REQUESTED, ACCEPTED, REJECTED, ENDED
}