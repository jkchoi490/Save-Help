package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_necessities_subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구독한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 특정 생필품을 구독하는 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "necessity_id")
    private DailyNecessities necessity;

    // 카테고리 단위 구독
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private DailyNecessitiesCategory category;

    // 재입고 알림 여부
    @Builder.Default
    private Boolean notifyOnRestock = true;

    // 상태 변경 알림 여부
    @Builder.Default
    private Boolean notifyOnStatusChange = true;

    // 가격 변경 알림 여부
    @Builder.Default
    private Boolean notifyOnPriceChange = false;

    @Builder.Default
    private Boolean active = true;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (active == null) {
            active = true;
        }
        if (notifyOnRestock == null) {
            notifyOnRestock = true;
        }
        if (notifyOnStatusChange == null) {
            notifyOnStatusChange = true;
        }
        if (notifyOnPriceChange == null) {
            notifyOnPriceChange = false;
        }
    }
}