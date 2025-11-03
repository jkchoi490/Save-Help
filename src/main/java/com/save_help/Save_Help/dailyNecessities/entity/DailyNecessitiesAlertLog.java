package com.save_help.Save_Help.dailyNecessities.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class DailyNecessitiesAlertLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(length = 1000)
    private String message;

    private LocalDateTime createdAt;

    public enum AlertType {
        LOW_STOCK, EXPIRE_SOON, SYSTEM_ERROR
    }
}
