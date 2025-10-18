package com.save_help.Save_Help.messaging.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class DailyNecessitiesMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DailyNecessitiesMessageSession session;

    private String sender;  // "USER" or "CENTER"
    private String content;
    private LocalDateTime sentAt = LocalDateTime.now();
}