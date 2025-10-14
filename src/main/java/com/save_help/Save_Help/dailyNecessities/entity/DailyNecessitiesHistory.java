package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DailyNecessitiesHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DailyNecessities item;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 입출고 담당자

    private int quantity;

    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime timestamp = LocalDateTime.now();

    public DailyNecessitiesHistory(DailyNecessities item, User user, int quantity, Type type) {
        this.item = item;
        this.user = user;
        this.quantity = quantity;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public enum Type {
        IN, OUT
    }
}
