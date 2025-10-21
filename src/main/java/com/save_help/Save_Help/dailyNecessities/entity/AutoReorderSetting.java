package com.save_help.Save_Help.dailyNecessities.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AutoReorderSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;       // 사용자 ID
    private Long itemId;       // 생필품 ID
    private int quantity;      // 자동 신청 수량

    private int intervalDays;  // 자동 신청 주기 (ex: 7일마다)

    private LocalDateTime nextReorderAt;  // 다음 자동 신청 예정일

    private boolean active = true;        // 자동 신청 활성화 여부
}